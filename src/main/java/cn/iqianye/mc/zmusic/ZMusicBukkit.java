package cn.iqianye.mc.zmusic;

import cn.iqianye.mc.zmusic.api.Version;
import cn.iqianye.mc.zmusic.bstats.MetricsBukkit;
import cn.iqianye.mc.zmusic.command.CmdBukkit;
import cn.iqianye.mc.zmusic.config.Conf;
import cn.iqianye.mc.zmusic.config.load.LoadBukkit;
import cn.iqianye.mc.zmusic.mod.SendBukkit;
import cn.iqianye.mc.zmusic.music.PlayListPlayer;
import cn.iqianye.mc.zmusic.other.Val;
import cn.iqianye.mc.zmusic.papi.PApiHook;
import cn.iqianye.mc.zmusic.player.PlayerStatus;
import cn.iqianye.mc.zmusic.utils.OtherUtils;
import cn.iqianye.mc.zmusic.utils.log.LogBukkit;
import cn.iqianye.mc.zmusic.utils.message.MessageBukkit;
import cn.iqianye.mc.zmusic.utils.music.MusicBukkit;
import cn.iqianye.mc.zmusic.utils.player.PlayerBukkit;
import cn.iqianye.mc.zmusic.utils.runnable.RunTaskBukkit;
import cn.iqianye.mc.zmusic.utils.server.ServerBukkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

public class ZMusicBukkit extends JavaPlugin implements Listener {

    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        ZMusic.isBC = false;
        ZMusic.log = new LogBukkit(getServer().getConsoleSender());
        ZMusic.runTask = new RunTaskBukkit();
        ZMusic.message = new MessageBukkit();
        ZMusic.music = new MusicBukkit();
        ZMusic.send = new SendBukkit();
        ZMusic.player = new PlayerBukkit();
        ZMusic.server = new ServerBukkit();
        ZMusic.dataFolder = getDataFolder();
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        Conf.debug = true;
        Val.thisVer = getDescription().getVersion();
        Version version = new Version();
        ZMusic.log.sendNormalMessage("正在加载中....");
        //注册bStats
        MetricsBukkit bStats = new MetricsBukkit(this, 7291);
        //注册命令对应的执行器
        getCommand("zm").setExecutor(new CmdBukkit());
        //注册命令对应的自动补全器
        getCommand("zm").setTabCompleter(new CmdBukkit());
        if (getServer().getPluginManager().isPluginEnabled("AudioBuffer")) {
            ZMusic.log.sendErrorMessage("请勿安装AudioBuffer插件.");
            Val.isEnable = false;
        }
        if (getServer().getPluginManager().isPluginEnabled("AllMusic")) {
            ZMusic.log.sendErrorMessage("请勿安装AllMusic插件.");
            Val.isEnable = false;
        }
        //注册Mod通信频道
        ZMusic.log.sendNormalMessage("正在注册Mod通信频道...");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "allmusic:channel");
        ZMusic.log.sendNormalMessage("-- §r[§eAllMusic§r]§a 频道注册完毕.");
        if (!version.isHigherThan("1.12")) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, "AudioBuffer");
            ZMusic.log.sendNormalMessage("-- §r[§eAudioBuffer§r]§a 频道注册完毕.");
        } else {
            ZMusic.log.sendErrorMessage("-- §r[§eAudioBuffer§r]§c 服务端大于1.12，频道注册取消.");
        }
        //注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);
        OtherUtils.checkUpdate();
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            ZMusic.log.sendNormalMessage("已检测到§ePlaceholderAPI§a, 正在注册...");
            boolean success = new PApiHook().register();
            if (success) {
                ZMusic.log.sendNormalMessage("-- §r[§ePlaceholderAPI§r] §a注册成功!");
            } else {
                ZMusic.log.sendErrorMessage("-- §r[§ePlaceholderAPI§r] §c注册失败!");
            }
        } else {
            ZMusic.log.sendErrorMessage("未找到§ePlaceholderAPI§c, §ePlaceholderAPI§c相关功能不生效.");
        }
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            ZMusic.log.sendNormalMessage("已检测到Vault, 经济功能生效.");
        } else {
            ZMusic.log.sendErrorMessage("未找到Vault, 经济相关功能不生效.");
            Conf.realSupportVault = false;
        }
        if (getServer().getPluginManager().isPluginEnabled("ViaVersion")) {
            ZMusic.log.sendNormalMessage("已检测到ViaVersion, 高版本转发功能生效.");
        } else {
            ZMusic.log.sendErrorMessage("未找到ViaVersion, 高版本转发功能不生效.");
            Val.isViaVer = false;
        }
        if (version.isLowerThan("1.8")) {
            if (version.isEquals("1.7.10")) {
                if (!Bukkit.getName().contains("Uranium")) {
                    ZMusic.log.sendErrorMessage("检测到当前服务端非Uranium，不支持Title/ActionBar显示");
                    Conf.realSupportTitle = false;
                    Conf.realSupportActionBar = false;
                }
            } else {
                ZMusic.log.sendErrorMessage("检测到当前服务端版本低于1.8，不支持Title/ActionBar显示");
                Conf.realSupportTitle = false;
                Conf.realSupportActionBar = false;
            }
        }
        if (version.isLowerThan("1.9")) {
            ZMusic.log.sendErrorMessage("检测到当前服务端版本低于1.9，不支持BossBar");
            Conf.realSupportBossBar = false;
        }
        if (version.isLowerThan("1.12")) {
            ZMusic.log.sendErrorMessage("检测到当前服务端版本低于1.12，不支持Hud显示");
            Conf.realSupportHud = false;
            ZMusic.log.sendErrorMessage("检测到当前服务端版本低于1.12，不支持进度");
            Conf.realSupportAdvancement = false;
        }
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
            ZMusic.log.sendErrorMessage("无法找到配置文件,正在创建!");
        }
        reloadConfig();
        LoadBukkit.load(getConfig());
        OtherUtils.loginNetease();
        ZMusic.log.sendNormalMessage("成功加载配置文件!");
        ZMusic.log.sendNormalMessage("插件作者: 真心");
        ZMusic.log.sendNormalMessage("博客：www.zhenxin.xyz");
        ZMusic.log.sendNormalMessage("QQ：1307993674");
        ZMusic.log.sendNormalMessage("插件交流群：1032722724");
        ZMusic.log.sendNormalMessage("插件已加载完成!");
    }

    @Override
    public void onDisable() {
        ZMusic.log.sendNormalMessage("正在卸载中....");
        List<Player> players = new ArrayList<>(getServer().getOnlinePlayers());
        if (!players.isEmpty()) {
            for (Player player : players) {
                OtherUtils.resetPlayerStatus(player);
                PlayListPlayer plp = PlayerStatus.getPlayerPlayListPlayer(player);
                if (plp != null) {
                    plp.isStop = true;
                    PlayerStatus.setPlayerPlayListPlayer(player, null);
                    OtherUtils.resetPlayerStatus(player);
                }
            }
        }
        ZMusic.log.sendNormalMessage("插件作者: 真心");
        ZMusic.log.sendNormalMessage("博客：www.zhenxin.xyz");
        ZMusic.log.sendNormalMessage("QQ：1307993674");
        ZMusic.log.sendNormalMessage("插件交流群：1032722724");
        ZMusic.log.sendNormalMessage("插件已卸载完成!");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        ZMusic.runTask.start(() -> {
            Player player = event.getPlayer();
            if (ZMusic.player.hasPermission(player, "zmusic.admin") || player.isOp()) {
                OtherUtils.checkUpdate();
                if (!Val.isLatest) {
                    ZMusic.message.sendNormalMessage("发现新版本 V" + Val.latestVer, player);
                    ZMusic.message.sendNormalMessage("更新日志:", player);
                    String[] updateLog = Val.updateLog.split("\\n");
                    for (String s : updateLog) {
                        ZMusic.message.sendNormalMessage(s, player);
                    }
                    ZMusic.message.sendNormalMessage("下载地址: " + ChatColor.YELLOW + ChatColor.UNDERLINE + Val.downloadUrl, player);
                }
            }
        });
    }

}
