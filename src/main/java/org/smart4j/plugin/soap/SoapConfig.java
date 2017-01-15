package org.smart4j.plugin.soap;

import org.smart4j.framework.helper.ConfigHelper;

/**
 * Created by shijiapeng on 17/1/9.
 */
public class SoapConfig {

    public static boolean isLog() {
        return ConfigHelper.getBoolean(SoapConstant.LOG);
    }
}
