/*
 * Copyright (C) 2016 The Android Open Source Project
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

import static android.support.car.CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION;

import android.support.car.Car;
import android.support.car.CarAppFocusManager;
import android.support.car.CarAppFocusManager.AppFocusChangeListener;
import android.support.car.CarAppFocusManager.AppFocusOwnershipChangeListener;
import android.support.car.navigation.CarNavigationInstrumentCluster;
import android.support.car.navigation.CarNavigationStatusManager;
import android.support.car.navigation.CarNavigationStatusManager.CarNavigationListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for {@link android.support.car.navigation.CarNavigationStatusManager}
 */
public class CarNavigationStatusManagerTest extends CarApiTestBase {

    private CarNavigationStatusManager mCarNavigationStatusManager;
    private CarAppFocusManager mCarAppFocusManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCarNavigationStatusManager =
                (CarNavigationStatusManager) getCar().getCarManager(Car.CAR_NAVIGATION_SERVICE);
        assertNotNull(mCarNavigationStatusManager);
        mCarAppFocusManager =
                (CarAppFocusManager) getCar().getCarManager(Car.APP_FOCUS_SERVICE);
        assertNotNull(mCarAppFocusManager);
    }

    public void testStart() throws Exception {
        final CountDownLatch onStartLatch = new CountDownLatch(1);

        mCarNavigationStatusManager.registerListener(new CarNavigationListener() {
            @Override
            public void onInstrumentClusterStart(CarNavigationInstrumentCluster instrumentCluster) {
                // TODO: we should use VehicleHalMock once we implement HAL support in
                // CarNavigationStatusService.
                assertFalse(instrumentCluster.supportsCustomImages());
                assertEquals(1000, instrumentCluster.getMinIntervalMs());
                onStartLatch.countDown();
            }

            @Override
            public void onInstrumentClusterStop() {
              // TODO
            }
        });

        assertTrue(onStartLatch.await(DEFAULT_WAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS));

        try {
            mCarNavigationStatusManager.sendNavigationStatus(1);
            fail();
        } catch (IllegalStateException expected) {
            // Expected. Client should acquire focus ownership for APP_FOCUS_TYPE_NAVIGATION.
        }

        mCarAppFocusManager.registerFocusListener(new AppFocusChangeListener() {
            @Override
            public void onAppFocusChange(int appType, boolean active) {
                // Nothing to do here.
            }
        }, APP_FOCUS_TYPE_NAVIGATION);
        AppFocusOwnershipChangeListener ownershipListener = new AppFocusOwnershipChangeListener() {
            @Override
            public void onAppFocusOwnershipLoss(int focus) {
                // Nothing to do here.
            }
        };
        mCarAppFocusManager.requestAppFocus(ownershipListener, APP_FOCUS_TYPE_NAVIGATION);
        assertTrue(mCarAppFocusManager.isOwningFocus(ownershipListener, APP_FOCUS_TYPE_NAVIGATION));

        // TODO: we should use mocked HAL to be able to verify this, right now just make sure that
        // it is not crashing and logcat has appropriate traces.
        mCarNavigationStatusManager.sendNavigationStatus(1);
    }
}