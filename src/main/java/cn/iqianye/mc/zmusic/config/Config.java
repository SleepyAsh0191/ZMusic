package cn.iqianye.mc.zmusic.config;

import cn.iqianye.mc.zmusic.Main;
import cn.iqianye.mc.zmusic.utils.LogUtils;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config {
    // Prefix
    public static String prefix = ChatColor.AQUA + "ZMusic " + ChatColor.YELLOW + ">>> " + ChatColor.RESET;
    // Version
    public static int version;
    // LatestVersion
    public static int latestVersion = 3;
    // Debug
    public static boolean debug;
    // Account
    public static String neteaseloginType;
    public static String neteaseAccount;
    public static String neteasePassword;
    public static boolean neteaseFollow;
    public static String neteasePasswordType;
    // Music
    public static int money;
    public static int cooldown;
    // Lyric
    public static boolean lyricEnable;
    public static boolean showLyricTr;
    public static boolean supportBossBar = false;
    public static boolean supportActionBar = false;
    public static boolean supportTitle = false;
    public static boolean supportChat = false;

    // RealSupport
    public static boolean realSupportTitle = true;
    public static boolean realSupportVault = true;

    public static void load(FileConfiguration configuration) {
        // Version
        version = configuration.getInt("version");
        if (version != latestVersion) {
            LogUtils.sendNormalMessage("-- 正在更新配置文件...");
            File config = new File(JavaPlugin.getPlugin(Main.class).getDataFolder() + File.separator + "config.yml");
            LogUtils.sendNormalMessage("-- 正在删除原配置文件...");
            config.delete();
            LogUtils.sendNormalMessage("-- 正在释放新配置文件...");
            JavaPlugin.getPlugin(Main.class).saveDefaultConfig();
            LogUtils.sendNormalMessage("-- 更新完毕.");
            JavaPlugin.getPlugin(Main.class).reloadConfig();
        }
        // Debug
        debug = configuration.getBoolean("debug");
        // Account
        neteaseloginType = configuration.getString("account.netease.loginType");
        neteaseAccount = configuration.getString("account.netease.account");
        neteasePasswordType = configuration.getString("account.netease.passwordType");
        if (neteasePasswordType.equalsIgnoreCase("normal")) {
            neteasePassword = OtherUtils.getMD5String(configuration.getString("account.netease.password"));
        } else if (neteasePasswordType.equalsIgnoreCase("md5")) {
            neteasePassword = configuration.getString("account.netease.password");
        }
        neteaseFollow = configuration.getBoolean("account.netease.follow");
        // Music
        money = configuration.getInt("music.money");
        cooldown = configuration.getInt("music.cooldown");
        // Lyric
        lyricEnable = configuration.getBoolean("lyric.enable");
        showLyricTr = configuration.getBoolean("lyric.showLyricTr");
        supportBossBar = configuration.getBoolean("lyric.bossBar");
        supportActionBar = configuration.getBoolean("lyric.actionBar");
        if (realSupportTitle) {
            supportTitle = configuration.getBoolean("lyric.subTitle");
        }
        supportChat = configuration.getBoolean("lyric.chatMessage");
    }
}