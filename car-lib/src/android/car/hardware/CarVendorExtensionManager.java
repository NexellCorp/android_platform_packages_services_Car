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

package android.car.hardware;

import android.annotation.SystemApi;
import android.car.Car;
import android.car.CarManagerBase;
import android.car.CarNotConnectedException;
import android.car.hardware.property.CarPropertyManagerBase;
import android.car.hardware.property.CarPropertyManagerBase.CarPropertyEventListener;
import android.os.IBinder;
import android.os.Looper;
import android.util.ArraySet;

import com.android.internal.annotations.GuardedBy;

import java.util.List;

/**
 * API to access custom vehicle properties defined by OEMs.
 * <p>
 * System permission {@link Car#PERMISSION_VENDOR_EXTENSION} is required to get this manager.
 * </p>
 * @hide
 */
@SystemApi
public class CarVendorExtensionManager implements CarManagerBase {

    private final static boolean DBG = true;
    private final static String TAG = CarVendorExtensionManager.class.getSimpleName();
    private final CarPropertyManagerBase mPropertyManager;

    @GuardedBy("mLock")
    private ArraySet<CarVendorExtensionListener> mListeners;
    private final Object mLock = new Object();

    /**
     * Creates an instance of the {@link CarVendorExtensionManager}.
     *
     * <p>Should not be obtained directly by clients, use {@link Car#getCarManager(String)} instead.
     * @hide
     */
    public CarVendorExtensionManager(IBinder service, Looper looper) {
        mPropertyManager = new CarPropertyManagerBase(service, looper, DBG, TAG);
    }

    /**
     * Contains callback functions that will be called when some event happens with vehicle
     * property.
     */
    public interface CarVendorExtensionListener {
        /** Called when a property is updated */
        void onChangeEvent(CarPropertyValue value);

        /** Called when an error is detected with a property */
        void onErrorEvent(int propertyId, int zone);
    }

    /**
     * Registers listener. The methods of the listener will be called when new events arrived in
     * the main thread.
     */
    public void registerListener(CarVendorExtensionListener listener) throws CarNotConnectedException {
        synchronized (mLock) {
            if (mListeners == null) {
                mPropertyManager.registerListener(new CarPropertyEventListener() {
                    @Override
                    public void onChangeEvent(CarPropertyValue value) {
                        for (CarVendorExtensionListener listener: getListeners()) {
                            listener.onChangeEvent(value);
                        }
                    }

                    @Override
                    public void onErrorEvent(int propertyId, int zone) {
                        for (CarVendorExtensionListener listener: getListeners()) {
                            listener.onErrorEvent(propertyId, zone);
                        }
                    }
                });
                mListeners = new ArraySet<>(1 /* We expect at least one element */);
            }
            mListeners.add(listener);
        }
    }

    /** Unregisters listener that was previously registered. */
    public void unregisterListener(CarVendorExtensionListener listener)
            throws CarNotConnectedException {
        synchronized (mLock) {
            mListeners.remove(listener);
            if (mListeners.isEmpty()) {
                mPropertyManager.unregisterListener();
                mListeners = null;
            }
        }
    }

    /** Returns copy of listeners. Thread safe. */
    private CarVendorExtensionListener[] getListeners() {
        synchronized (mLock) {
            return mListeners.toArray(new CarVendorExtensionListener[mListeners.size()]);
        }
    }

    public List<CarPropertyConfig> getProperties() throws CarNotConnectedException {
        return mPropertyManager.getPropertyList();
    }

    /**
     * Returns property value. Use this function for global vehicle properties.
     *
     * @param propertyClass - data type of the given property, for example property that was
     *        defined as {@code VEHICLE_VALUE_TYPE_INT32} in vehicle HAL could be accessed using
     *        {@code Integer.class}.
     * @param propId - property id which is matched with the one defined in vehicle HAL
     *
     * @throws CarNotConnectedException
     */
    public <E> E getGlobalProperty(Class<E> propertyClass, int propId)
            throws CarNotConnectedException {
        return getProperty(propertyClass, propId, 0 /* area */);
    }

    /**
     * Returns property value. Use this function for "zoned" vehicle properties.
     *
     * @param propertyClass - data type of the given property, for example property that was
     *        defined as {@code VEHICLE_VALUE_TYPE_INT32} in vehicle HAL could be accessed using
     *        {@code Integer.class}.
     * @param propId - property id which is matched with the one defined in vehicle HAL
     * @param area - vehicle area (e.g. {@code VEHICLE_ZONE_ROW_1_LEFT}
     *        or {@code VEHICLE_MIRROR_DRIVER_LEFT}
     *
     * @throws CarNotConnectedException
     */
    public <E> E getProperty(Class<E> propertyClass, int propId, int area)
            throws CarNotConnectedException {
        return mPropertyManager.getProperty(propertyClass, propId, area).getValue();
    }

    /**
     * Call this function to set a value to global vehicle property.
     *
     * @param propertyClass - data type of the given property, for example property that was
     *        defined as {@code VEHICLE_VALUE_TYPE_INT32} in vehicle HAL could be accessed using
     *        {@code Integer.class}.
     * @param propId - property id which is matched with the one defined in vehicle HAL
     * @param value - new value, this object should match a class provided in {@code propertyClass}
     *        argument.
     *
     * @throws CarNotConnectedException
     */
    public <E> void setGlobalProperty(Class<E> propertyClass, int propId, E value)
            throws CarNotConnectedException {
        mPropertyManager.setProperty(propertyClass, propId, 0 /* area */, value);
    }

    /**
     * Call this function to set a value to "zoned" vehicle property.
     *
     * @param propertyClass - data type of the given property, for example property that was
     *        defined as {@code VEHICLE_VALUE_TYPE_INT32} in vehicle HAL could be accessed using
     *        {@code Integer.class}.
     * @param propId - property id which is matched with the one defined in vehicle HAL
     * @param area - vehicle area (e.g. {@code VEHICLE_ZONE_ROW_1_LEFT}
     *        or {@code VEHICLE_MIRROR_DRIVER_LEFT}
     * @param value - new value, this object should match a class provided in {@code propertyClass}
     *        argument.
     *
     * @throws CarNotConnectedException
     */
    public <E> void setProperty(Class<E> propertyClass, int propId, int area, E value)
            throws CarNotConnectedException {
        mPropertyManager.setProperty(propertyClass, propId, area, value);
    }

    /** @hide */
    @Override
    public void onCarDisconnected() {
        mPropertyManager.onCarDisconnected();
    }
}