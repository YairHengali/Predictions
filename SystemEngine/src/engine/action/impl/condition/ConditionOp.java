package engine.action.impl.condition;

import engine.property.PropertyType;

public enum ConditionOp {

    EQUALS{
        @Override
        public boolean eval(String propertyValue, String value, PropertyType propertyType)
        {
            switch (propertyType){
                case BOOLEAN:
                    return Boolean.valueOf(propertyValue) == Boolean.valueOf(value);
                case DECIMAL:
                    return Integer.parseInt(propertyValue) == Integer.parseInt(value);
                case FLOAT:
                    return Float.parseFloat(propertyValue) == Float.parseFloat(value);
                case STRING:
                    return propertyValue.equals(value);
            }
            return false;
        }
    },
    NOTEQUALS {
        @Override
        public boolean eval(String propertyValue, String value, PropertyType propertyType) {
            switch (propertyType){
                case BOOLEAN:
                    return Boolean.valueOf(propertyValue) != Boolean.valueOf(value);
                case DECIMAL:
                    return Integer.parseInt(propertyValue) != Integer.parseInt(value);
                case FLOAT:
                    return Float.parseFloat(propertyValue) != Float.parseFloat(value);
                case STRING:
                    return !(propertyValue.equals(value));
            }
            return false;
        }
    },
    BT {
        @Override
        public boolean eval(String propertyValue, String value, PropertyType propertyType) {
            switch (propertyType){
                case DECIMAL:
                    return Integer.parseInt(propertyValue) > Integer.parseInt(value);
                case FLOAT:
                    return Float.parseFloat(propertyValue) > Float.parseFloat(value);
            }

        }
    },
    LT {
        @Override
        public boolean eval(String propertyValue, String value, PropertyType propertyType) {
            switch (propertyType){
                case DECIMAL:
                    return Integer.parseInt(propertyValue) < Integer.parseInt(value);
                case FLOAT:
                    return Float.parseFloat(propertyValue) < Float.parseFloat(value);
            }
        }
    };

    public abstract boolean eval(String propertyValue, String value, PropertyType propertyType);
//    EQUALS{
//        @Override
//        public boolean eval(Object propertyValue, Object value, PropertyType propertyType)
//        {
//            switch (propertyType){
//                case BOOLEAN:
//                    return (Boolean)propertyValue == (Boolean)value;
//                case DECIMAL:
//                    return (Integer)propertyValue == (Integer)value;
//                case FLOAT:
//                    return (Float)propertyValue == (Float)value;
//                case STRING:
//                    return ((String)propertyValue).equals( ((String)value) );
//            }
//            return false;
//        }
//    },
//    NOTEQUALS {
//        @Override
//        public boolean eval(Object propertyValue, Object value, PropertyType propertyType) {
//            switch (propertyType){
//                case BOOLEAN:
//                    return (Boolean)propertyValue != (Boolean)value;
//                case DECIMAL:
//                    return (Integer)propertyValue != (Integer)value;
//                case FLOAT:
//                    return (Float)propertyValue != (Float)value;
//                case STRING:
//                    return !((String)propertyValue).equals( ((String)value) );
//            }
//            return false;
//        }
//    },
//    BT {
//        @Override
//        public boolean eval(Object propertyValue, Object value, PropertyType propertyType) {
//            switch (propertyType){
//                case DECIMAL:
//                    return (Integer)propertyValue > (Integer)value;
//                case FLOAT:
//                    return (Float)propertyValue > (Float)value;
//                case STRING:
//                    return ((String)propertyValue).compareTo( ((String)value) ) > 0;
//            }
//            return false;
//        }
//    },
//    LT {
//        @Override
//        public boolean eval(Object propertyValue, Object value, PropertyType propertyType) {
//            switch (propertyType){
//                case DECIMAL:
//                    return (Integer)propertyValue < (Integer)value;
//                case FLOAT:
//                    return (Float)propertyValue < (Float)value;
//                case STRING:
//                    return ((String)propertyValue).compareTo( ((String)value) ) < 0;
//            }
//            return false;
//        }
//    };
//
//    public abstract boolean eval(Object propertyValue, Object value, PropertyType propertyType);
}
