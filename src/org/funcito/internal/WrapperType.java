package org.funcito.internal;

public enum WrapperType {
    GUAVA_FUNCTION, GUAVA_PREDICATE, FJ_F;
    
    public String toString() {
        String result = "";
        
        if (this == GUAVA_FUNCTION) {
            result = "Guava Function";
        } else if (this == GUAVA_PREDICATE) {
            result = "Guava Predicate";            
        } else if (this == FJ_F) {
            result = "Functional Java F (function)";
        }
        
        return result;
    }
}
