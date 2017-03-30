# Copyright 2017 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

"""
    This file contains constants defined in hardware/interfaces/vehicle/2.0/types.hal

    Constants in this file are parsed from:
        out/soong/.intermediates/hardware/interfaces/automotive/vehicle/2.0/android.hardware.automotive.vehicle@2.0_genc++_headers/gen/android/hardware/automotive/vehicle/2.0/types.h

    Currently, there is no script to auto-generate this constants file.  The file is generated by
    copying enum fields into an editor and running a macro to format it.  The elements being used
    are shown in the following table:

        type.h file:                 this file:
        VehiclePropertyType enum --> VEHICLE_VALUE_TYPE_*
        VehicleProperty enum     --> VEHICLE_PROPERTY_*
        VehicleAreaZone enum     --> VEHICLE_ZONE_*
        VehiclePropertyType enum --> class vhal_types_2_0
"""

# Vehicle Property ID
VEHICLE_PROPERTY_INFO_VIN = 286261504
VEHICLE_PROPERTY_INFO_MAKE = 286261505
VEHICLE_PROPERTY_INFO_MODEL = 286261506
VEHICLE_PROPERTY_INFO_MODEL_YEAR = 289407235
VEHICLE_PROPERTY_INFO_FUEL_CAPACITY = 291504388
VEHICLE_PROPERTY_PERF_ODOMETER = 291504644
VEHICLE_PROPERTY_PERF_VEHICLE_SPEED = 291504647
VEHICLE_PROPERTY_ENGINE_COOLANT_TEMP = 291504897
VEHICLE_PROPERTY_ENGINE_OIL_TEMP = 291504900
VEHICLE_PROPERTY_ENGINE_RPM = 291504901
VEHICLE_PROPERTY_GEAR_SELECTION = 289408000
VEHICLE_PROPERTY_CURRENT_GEAR = 289408001
VEHICLE_PROPERTY_PARKING_BRAKE_ON = 287310850
VEHICLE_PROPERTY_DRIVING_STATUS = 289408004
VEHICLE_PROPERTY_FUEL_LEVEL_LOW = 287310853
VEHICLE_PROPERTY_NIGHT_MODE = 287310855
VEHICLE_PROPERTY_TURN_SIGNAL_STATE = 289408008
VEHICLE_PROPERTY_IGNITION_STATE = 289408009
VEHICLE_PROPERTY_HVAC_FAN_SPEED = 306185472
VEHICLE_PROPERTY_HVAC_FAN_DIRECTION = 306185473
VEHICLE_PROPERTY_HVAC_TEMPERATURE_CURRENT = 308282626
VEHICLE_PROPERTY_HVAC_TEMPERATURE_SET = 308282627
VEHICLE_PROPERTY_HVAC_DEFROSTER = 320865540
VEHICLE_PROPERTY_HVAC_AC_ON = 304088325
VEHICLE_PROPERTY_HVAC_MAX_AC_ON = 304088326
VEHICLE_PROPERTY_HVAC_MAX_DEFROST_ON = 304088327
VEHICLE_PROPERTY_HVAC_RECIRC_ON = 304088328
VEHICLE_PROPERTY_HVAC_DUAL_ON = 304088329
VEHICLE_PROPERTY_HVAC_AUTO_ON = 304088330
VEHICLE_PROPERTY_HVAC_SEAT_TEMPERATURE = 356517131
VEHICLE_PROPERTY_HVAC_SIDE_MIRROR_HEAT = 339739916
VEHICLE_PROPERTY_HVAC_STEERING_WHEEL_TEMP = 289408269
VEHICLE_PROPERTY_HVAC_TEMPERATURE_UNITS = 306185486
VEHICLE_PROPERTY_HVAC_ACTUAL_FAN_SPEED_RPM = 306185487
VEHICLE_PROPERTY_HVAC_FAN_DIRECTION_AVAILABLE = 306185489
VEHICLE_PROPERTY_HVAC_POWER_ON = 304088336
VEHICLE_PROPERTY_ENV_OUTSIDE_TEMPERATURE = 291505923
VEHICLE_PROPERTY_ENV_CABIN_TEMPERATURE = 291505924
VEHICLE_PROPERTY_RADIO_PRESET = 289474561
VEHICLE_PROPERTY_AUDIO_FOCUS = 289474816
VEHICLE_PROPERTY_AUDIO_FOCUS_EXT_SYNC = 289474832
VEHICLE_PROPERTY_AUDIO_VOLUME = 289474817
VEHICLE_PROPERTY_AUDIO_VOLUME_EXT_SYNC = 289474833
VEHICLE_PROPERTY_AUDIO_VOLUME_LIMIT = 289474818
VEHICLE_PROPERTY_AUDIO_ROUTING_POLICY = 289474819
VEHICLE_PROPERTY_AUDIO_HW_VARIANT = 289409284
VEHICLE_PROPERTY_AUDIO_EXT_ROUTING_HINT = 289474821
VEHICLE_PROPERTY_AUDIO_STREAM_STATE = 289474822
VEHICLE_PROPERTY_AUDIO_PARAMETERS = 286263559
VEHICLE_PROPERTY_AP_POWER_STATE = 2560
VEHICLE_PROPERTY_DISPLAY_BRIGHTNESS = 289409537
VEHICLE_PROPERTY_AP_POWER_BOOTUP_REASON = 289409538
VEHICLE_PROPERTY_HW_KEY_INPUT = 289475088
VEHICLE_PROPERTY_INSTRUMENT_CLUSTER_INFO = 289475104
VEHICLE_PROPERTY_UNIX_TIME = 290458160
VEHICLE_PROPERTY_CURRENT_TIME_IN_SECONDS = 289409585
VEHICLE_PROPERTY_DOOR_POS = 373295872
VEHICLE_PROPERTY_DOOR_MOVE = 373295873
VEHICLE_PROPERTY_DOOR_LOCK = 371198722
VEHICLE_PROPERTY_MIRROR_Z_POS = 339741504
VEHICLE_PROPERTY_MIRROR_Z_MOVE = 339741505
VEHICLE_PROPERTY_MIRROR_Y_POS = 339741506
VEHICLE_PROPERTY_MIRROR_Y_MOVE = 339741507
VEHICLE_PROPERTY_MIRROR_LOCK = 287312708
VEHICLE_PROPERTY_MIRROR_FOLD = 287312709
VEHICLE_PROPERTY_SEAT_MEMORY_SELECT = 356518784
VEHICLE_PROPERTY_SEAT_MEMORY_SET = 356518785
VEHICLE_PROPERTY_SEAT_BELT_BUCKLED = 354421634
VEHICLE_PROPERTY_SEAT_BELT_HEIGHT_POS = 356518787
VEHICLE_PROPERTY_SEAT_BELT_HEIGHT_MOVE = 356518788
VEHICLE_PROPERTY_SEAT_FORE_AFT_POS = 356518789
VEHICLE_PROPERTY_SEAT_FORE_AFT_MOVE = 356518790
VEHICLE_PROPERTY_SEAT_BACKREST_ANGLE_1_POS = 356518791
VEHICLE_PROPERTY_SEAT_BACKREST_ANGLE_1_MOVE = 356518792
VEHICLE_PROPERTY_SEAT_BACKREST_ANGLE_2_POS = 356518793
VEHICLE_PROPERTY_SEAT_BACKREST_ANGLE_2_MOVE = 356518794
VEHICLE_PROPERTY_SEAT_HEIGHT_POS = 356518795
VEHICLE_PROPERTY_SEAT_HEIGHT_MOVE = 356518796
VEHICLE_PROPERTY_SEAT_DEPTH_POS = 356518797
VEHICLE_PROPERTY_SEAT_DEPTH_MOVE = 356518798
VEHICLE_PROPERTY_SEAT_TILT_POS = 356518799
VEHICLE_PROPERTY_SEAT_TILT_MOVE = 356518800
VEHICLE_PROPERTY_SEAT_LUMBAR_FORE_AFT_POS = 356518801
VEHICLE_PROPERTY_SEAT_LUMBAR_FORE_AFT_MOVE = 356518802
VEHICLE_PROPERTY_SEAT_LUMBAR_SIDE_SUPPORT_POS = 356518803
VEHICLE_PROPERTY_SEAT_LUMBAR_SIDE_SUPPORT_MOVE = 356518804
VEHICLE_PROPERTY_SEAT_HEADREST_HEIGHT_POS = 289409941
VEHICLE_PROPERTY_SEAT_HEADREST_HEIGHT_MOVE = 356518806
VEHICLE_PROPERTY_SEAT_HEADREST_ANGLE_POS = 356518807
VEHICLE_PROPERTY_SEAT_HEADREST_ANGLE_MOVE = 356518808
VEHICLE_PROPERTY_SEAT_HEADREST_FORE_AFT_POS = 356518809
VEHICLE_PROPERTY_SEAT_HEADREST_FORE_AFT_MOVE = 356518810
VEHICLE_PROPERTY_WINDOW_POS = 289409984
VEHICLE_PROPERTY_WINDOW_MOVE = 289409985
VEHICLE_PROPERTY_WINDOW_VENT_POS = 289409986
VEHICLE_PROPERTY_WINDOW_VENT_MOVE = 289409987
VEHICLE_PROPERTY_WINDOW_LOCK = 287312836
VEHICLE_PROPERTY_VEHICLE_MAPS_DATA_SERVICE = 299895808
VEHICLE_PROPERTY_OBD2_LIVE_FRAME = 299896064
VEHICLE_PROPERTY_OBD2_FREEZE_FRAME = 299896065

# Vehicle Value Type
VEHICLE_VALUE_TYPE_STRING       = 0x00100000
VEHICLE_VALUE_TYPE_BOOLEAN      = 0x00200000
VEHICLE_VALUE_TYPE_INT32        = 0x00400000
VEHICLE_VALUE_TYPE_INT32_VEC    = 0x00410000
VEHICLE_VALUE_TYPE_INT64        = 0x00500000
VEHICLE_VALUE_TYPE_FLOAT        = 0x00600000
VEHICLE_VALUE_TYPE_FLOAT_VEC    = 0x00610000
VEHICLE_VALUE_TYPE_BYTES        = 0x00700000
VEHICLE_VALUE_TYPE_COMPLEX      = 0x00E00000

# Vehicle zone / area definitions
VEHICLE_ZONE_ROW_1_LEFT    = 0x00000001
VEHICLE_ZONE_ROW_1_CENTER  = 0x00000002
VEHICLE_ZONE_ROW_1_RIGHT   = 0x00000004
VEHICLE_ZONE_ROW_1_ALL     = 0x00000008
VEHICLE_ZONE_ROW_2_LEFT    = 0x00000010
VEHICLE_ZONE_ROW_2_CENTER  = 0x00000020
VEHICLE_ZONE_ROW_2_RIGHT   = 0x00000040
VEHICLE_ZONE_ROW_2_ALL     = 0x00000080
VEHICLE_ZONE_ROW_3_LEFT    = 0x00000100
VEHICLE_ZONE_ROW_3_CENTER  = 0x00000200
VEHICLE_ZONE_ROW_3_RIGHT   = 0x00000400
VEHICLE_ZONE_ROW_3_ALL     = 0x00000800
VEHICLE_ZONE_ROW_4_LEFT    = 0x00001000
VEHICLE_ZONE_ROW_4_CENTER  = 0x00002000
VEHICLE_ZONE_ROW_4_RIGHT   = 0x00004000
VEHICLE_ZONE_ROW_4_ALL     = 0x00008000
VEHICLE_ZONE_ALL           = 0x80000000

# Create a container of value_type constants to be used by vhal_emulator
class vhal_types_2_0:
    TYPE_STRING =  [VEHICLE_VALUE_TYPE_STRING]
    TYPE_BYTES  =  [VEHICLE_VALUE_TYPE_BYTES]
    TYPE_INT32  =  [VEHICLE_VALUE_TYPE_BOOLEAN,
                    VEHICLE_VALUE_TYPE_INT32]
    TYPE_INT64  =  [VEHICLE_VALUE_TYPE_INT64]
    TYPE_FLOAT  =  [VEHICLE_VALUE_TYPE_FLOAT]
    TYPE_INT32S =  [VEHICLE_VALUE_TYPE_INT32_VEC]
    TYPE_FLOATS =  [VEHICLE_VALUE_TYPE_FLOAT_VEC]
    TYPE_COMPLEX = [VEHICLE_VALUE_TYPE_COMPLEX]
