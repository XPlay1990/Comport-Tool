/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Config.HW_Config;

import java.util.HashMap;

/**
 *
 * @author jan.adamczyk
 */
public interface HW_Interface {

    /**
     *
     * @return
     */
    public HashMap<String, Object> getConfig();

    /**
     *
     * @param name
     * @param value
     */
    public void setParameter(String name, Object value);

    /**
     *
     * @param name
     */
    public void removeParameter(String name);
}
