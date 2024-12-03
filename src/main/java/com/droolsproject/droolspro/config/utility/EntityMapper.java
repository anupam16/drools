package com.droolsproject.droolspro.config.utility;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;

public class EntityMapper {

    public static void mapNonNullProperties(Object source, Object destination) {
        try {
            BeanUtilsBean notNullBeanUtils = new NotNullAwareBeanUtilsBean();
            notNullBeanUtils.copyProperties(destination, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static class NotNullAwareBeanUtilsBean extends BeanUtilsBean {
        @Override
        public void copyProperty(Object dest, String name, Object value)
                throws IllegalAccessException, InvocationTargetException {
            if (value != null) {
                super.copyProperty(dest, name, value);
            }
        }
    }
}
