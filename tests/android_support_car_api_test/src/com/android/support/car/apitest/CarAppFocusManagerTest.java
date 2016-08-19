/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.support.car.apitest;

import android.support.car.Car;
import android.support.car.CarAppFocusManager;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@MediumTest
public class CarAppFocusManagerTest extends CarApiTestBase {
    private static final String TAG = CarAppFocusManagerTest.class.getSimpleName();
    private CarAppFocusManager mManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mManager = (CarAppFocusManager) getCar().getCarManager(Car.APP_FOCUS_SERVICE);
        assertNotNull(mManager);

        // Request all application focuses and abandon them to ensure no active context is present
        // when test starts.
        FocusOwnershipChangeListener owner = new FocusOwnershipChangeListener();
        mManager.requestAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        mManager.requestAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        mManager.abandonAppFocus(owner);
    }

    public void testSetActiveNullListener() throws Exception {
        try {
            mManager.requestAppFocus(null, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testRegisterNull() throws Exception {
        try {
            mManager.registerFocusListener(null, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testRegisterUnregister() throws Exception {
        FocusChangeListener listener = new FocusChangeListener();
        FocusChangeListener listener2 = new FocusChangeListener();
        mManager.registerFocusListener(listener, 1);
        mManager.registerFocusListener(listener2, 1);
        mManager.unregisterFocusListener(listener);
        mManager.unregisterFocusListener(listener2);
    }

    public void testFocusChange() throws Exception {
        DefaultServiceConnectionListener connectionListener =
                new DefaultServiceConnectionListener();
        Car car2 = Car.createCar(getContext(), connectionListener, null);
        car2.connect();
        connectionListener.waitForConnection(DEFAULT_WAIT_TIMEOUT_MS);
        CarAppFocusManager manager2 = (CarAppFocusManager)
                car2.getCarManager(Car.APP_FOCUS_SERVICE);
        assertNotNull(manager2);
        final int[] emptyFocus = new int[0];

        FocusChangeListener change = new FocusChangeListener();
        FocusChangeListener change2 = new FocusChangeListener();
        FocusOwnershipChangeListener owner = new FocusOwnershipChangeListener();
        FocusOwnershipChangeListener owner2 = new FocusOwnershipChangeListener();
        mManager.registerFocusListener(change, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        mManager.registerFocusListener(change, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        manager2.registerFocusListener(change2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        manager2.registerFocusListener(change2, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);

        mManager.requestAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        assertTrue(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertFalse(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertFalse(manager2.isOwningFocus(owner2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertFalse(manager2.isOwningFocus(owner2,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertTrue(change2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, true));
        assertTrue(change.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, true));

        mManager.requestAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        assertTrue(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertTrue(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertFalse(manager2.isOwningFocus(owner2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertFalse(manager2.isOwningFocus(owner2,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertTrue(change2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, true));
        assertTrue(change.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, true));

        // this should be no-op
        change.reset();
        change2.reset();
        mManager.requestAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        assertFalse(change2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, true));
        assertFalse(change.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, true));

        manager2.requestAppFocus(owner2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        assertFalse(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertTrue(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertTrue(manager2.isOwningFocus(owner2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertFalse(manager2.isOwningFocus(owner2,
              CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertTrue(owner.waitForOwnershipLossAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));

        // no-op as it is not owning it
        change.reset();
        change2.reset();
        mManager.abandonAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        assertFalse(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertTrue(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertTrue(manager2.isOwningFocus(owner2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertFalse(manager2.isOwningFocus(owner2,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));

        change.reset();
        change2.reset();
        mManager.abandonAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        assertFalse(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertFalse(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertTrue(manager2.isOwningFocus(owner2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertFalse(manager2.isOwningFocus(owner2,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertTrue(change2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, false));
        assertTrue(change.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, false));

        change.reset();
        change2.reset();
        manager2.abandonAppFocus(owner2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        assertFalse(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertFalse(mManager.isOwningFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertFalse(manager2.isOwningFocus(owner2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION));
        assertFalse(manager2.isOwningFocus(owner2,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND));
        assertTrue(change.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, false));
        mManager.unregisterFocusListener(change);
        manager2.unregisterFocusListener(change2);
    }

    public void testFilter() throws Exception {
        DefaultServiceConnectionListener connectionListener =
                new DefaultServiceConnectionListener();
        Car car2 = Car.createCar(getContext(), connectionListener);
        car2.connect();
        connectionListener.waitForConnection(DEFAULT_WAIT_TIMEOUT_MS);
        CarAppFocusManager manager2 = (CarAppFocusManager)
                car2.getCarManager(Car.APP_FOCUS_SERVICE);
        assertNotNull(manager2);

        FocusChangeListener listener = new FocusChangeListener();
        FocusChangeListener listener2 = new FocusChangeListener();
        FocusOwnershipChangeListener owner = new FocusOwnershipChangeListener();
        mManager.registerFocusListener(listener, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        mManager.registerFocusListener(listener, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        manager2.registerFocusListener(listener2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);

        mManager.requestAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        assertTrue(listener.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                 CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, true));
        assertTrue(listener2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                 CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, true));

        listener.reset();
        listener2.reset();
        mManager.requestAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        assertTrue(listener.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, true));
        assertFalse(listener2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, true));

        listener.reset();
        listener2.reset();
        mManager.abandonAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        assertTrue(listener.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, false));
        assertFalse(listener2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, false));

        listener.reset();
        listener2.reset();
        mManager.abandonAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        assertTrue(listener.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, false));
        assertTrue(listener2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, false));
    }

    public void testMultipleChangeListenersPerManager() throws Exception {
        FocusChangeListener listener = new FocusChangeListener();
        FocusChangeListener listener2 = new FocusChangeListener();
        FocusOwnershipChangeListener owner = new FocusOwnershipChangeListener();
        mManager.registerFocusListener(listener, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        mManager.registerFocusListener(listener, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        mManager.registerFocusListener(listener2, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);

        mManager.requestAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        assertTrue(listener.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                 CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, true));
        assertTrue(listener2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                 CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, true));

        listener.reset();
        listener2.reset();
        mManager.requestAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        assertTrue(listener.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, true));
        assertFalse(listener2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, true));

        listener.reset();
        listener2.reset();
        mManager.abandonAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND);
        assertTrue(listener.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, false));
        assertFalse(listener2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_VOICE_COMMAND, false));

        listener.reset();
        listener2.reset();
        mManager.abandonAppFocus(owner, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION);
        assertTrue(listener.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, false));
        assertTrue(listener2.waitForFocusChangeAndAssert(DEFAULT_WAIT_TIMEOUT_MS,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION, false));
    }

    private class FocusChangeListener implements CarAppFocusManager.AppFocusChangeListener {
        private int mLastChangeAppType;
        private boolean mLastChangeAppActive;
        private final Semaphore mChangeWait = new Semaphore(0);

        public boolean waitForFocusChangeAndAssert(long timeoutMs, int expectedAppType,
                boolean expectedAppActive) throws Exception {
            if (!mChangeWait.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS)) {
                return false;
            }
            assertEquals(expectedAppType, mLastChangeAppType);
            assertEquals(expectedAppActive, mLastChangeAppActive);
            return true;
        }

        public void reset() {
            mLastChangeAppType = 0;
            mLastChangeAppActive = false;
        }

        @Override
        public void onAppFocusChange(int appType, boolean active) {
            Log.i(TAG, "onAppFocusChange appType=" + appType + " active=" + active);
            assertMainThread();
            mLastChangeAppType = appType;
            mLastChangeAppActive = active;
            mChangeWait.release();
        }
    }

    private class FocusOwnershipChangeListener
            implements CarAppFocusManager.AppFocusOwnershipChangeListener {
        private int mLastLossEvent;
        private final Semaphore mLossEventWait = new Semaphore(0);

        public boolean waitForOwnershipLossAndAssert(long timeoutMs, int expectedAppType)
                throws Exception {
            if (!mLossEventWait.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS)) {
                return false;
            }
            assertEquals(expectedAppType, mLastLossEvent);
            return true;
        }

        @Override
        public void onAppFocusOwnershipLoss(int appType) {
            Log.i(TAG, "onAppFocusOwnershipLoss " + appType);
            assertMainThread();
            mLastLossEvent = appType;
            mLossEventWait.release();
        }
    }
}