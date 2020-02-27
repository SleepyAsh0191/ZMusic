package cn.iqianye.MinecraftPlugins.ZMusic.Music.SearchSource;

import cn.iqianye.MinecraftPlugins.ZMusic.Utils.NetUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.util.Base64;

public class KuGouMusic {

    /**
     * 获取音乐链接
     *
     * @param musicName 音乐名称
     */
    public static JsonObject getMusicUrl(String musicName) {
        try {


            String getUrl =
                    "https://songsearch.kugou.com/song_search_v2?keyword="
                            +
                            URLEncoder.encode(musicName, "utf-8")
                            +
                            "&platform=WebFilter&format=json&page=1&pagesize=1";
            String jsonText = NetUtils.getNetString(getUrl, null);
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(jsonText, JsonObject.class);
            if (json.get("status").getAsInt() == 1) {
                JsonObject data = json.get("data").getAsJsonObject();
                JsonObject list = data.getAsJsonArray("lists").get(0).getAsJsonObject();
                String songName = list.get("SongName").getAsString();
                String songSinger = list.get("SingerName").getAsString();
                String hash = list.get("FileHash").getAsString();
                String id = list.get("ID").getAsString();
                String getInfoUrl = "http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=" + hash;
                String infoJsonText = NetUtils.getNetString(getInfoUrl, "http://m.kugou.com/play/info/" + id);
                JsonObject infoJosn = gson.fromJson(infoJsonText, JsonObject.class);
                String song_url = infoJosn.get("url").getAsString();
                String getLyricInfoUrl = "http://krcs.kugou.com/search?ver=1&man=yes&client=mobi&keyword=&duration=&hash=" + hash;
                String lyricInfoStr = NetUtils.getNetString(getLyricInfoUrl, null);
                JsonObject getLyrrcJson = gson.fromJson(lyricInfoStr, JsonObject.class);
                JsonObject candidates;
                String lyricID = "";
                String lyricAccessKey = "";
                try {
                    candidates = getLyrrcJson.getAsJsonArray("candidates").get(0).getAsJsonObject();
                    lyricID = candidates.get("id").getAsString();
                    lyricAccessKey = candidates.get("accesskey").getAsString();
                } catch (Exception e) {

                }
                String getLyricUrl = "http://lyrics.kugou.com/download?ver=1&id=" + lyricID + "&accesskey=" + lyricAccessKey + "&fmt=lrc&charset=utf8";
                String lyricStr = NetUtils.getNetString(getLyricUrl, null);
                JsonObject lyricJSON = gson.fromJson(lyricStr, JsonObject.class);
                String lyricBase64 = lyricJSON.get("content").getAsString();
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] bytes = decoder.decode(lyricBase64);
                String lyric = new String(bytes, "utf-8");
                lyric = lyric.replaceAll("\r", "");
                System.out.println(lyric);
                JsonObject returnObject = new JsonObject();
                returnObject.addProperty("name", songName);
                returnObject.addProperty("singer", songSinger);
                returnObject.addProperty("url", song_url);
                returnObject.addProperty("lyric", lyric);
                System.out.println();
                return returnObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
}
