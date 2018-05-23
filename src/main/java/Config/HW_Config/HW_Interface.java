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
    public HashMap<String, Object> getConfig();
    public void setParameter(String name, Object value);
    public void removeParameter(String name);
}
