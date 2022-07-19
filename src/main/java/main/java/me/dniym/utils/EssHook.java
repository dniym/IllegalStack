package main.java.me.dniym.utils;

import com.earth2me.essentials.utils.VersionUtil;

public class EssHook {

    public static boolean isHeight1_16() {
        return VersionUtil.getServerBukkitVersion().isHigherThan(VersionUtil.v1_16_1_R01);
    }

}
