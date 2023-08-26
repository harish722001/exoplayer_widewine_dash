package com.cognizant.mobilescreenshot;

import com.google.android.exoplayer2.C;

import java.util.UUID;

//public class Constants {
//    public static final String VIDEO_URL = "https://d4hwgxnar7vke.cloudfront.net/original_video.m3u8";
//    public static final String BASE_API_URL = "http://35.164.200.227:1821/";
//    public static final String API_END = "/detect_ads";
//    public static final String URL = "https://storage.googleapis.com/wvmedia/cenc/hevc/tears/tears.mpd";
//    public static final String DRM_LICENSE_URL = "https://proxy.uat.widevine.com/proxy?video_id=2015_tears&provider=widevine_test";
//    public static final String USER_AGENT = "ExoPlayer-Drm";
//    public static final UUID drmSchemeUuid = C.WIDEVINE_UUID;
//}

public class Constants {
    public static String URL = "https://tfmstest2.blob.core.windows.net/output/POC/POC_story4/VIKRAM/output16/dash_hevc_widevine/manifest-4k-connected-hevc-aac/manifest-4k-connected-hevc-aac.mpd";
    public static String DRM_LICENSE_URL = "https://zeefl7g9.anycast.nagra.com/ZEEFL7G9/wvls/contentlicenseservice/v1/licenses";
    public static final String USER_AGENT = "ExoPlayer-Drm";
    public static final UUID drmSchemeUuid = C.WIDEVINE_UUID;
    public static String requestHeaderKey = "nv-authorizations";
    public static String requestHeaderValue = "eyJraWQiOiI2NTcyODAiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZXIiOiIxLjAiLCJ0eXAiOiJDb250ZW50QXV0aFoiLCJjb250ZW50UmlnaHRzIjpbeyJjb250ZW50SWQiOiJ6ZWU1dGVzdCIsInVzYWdlUnVsZXNQcm9maWxlSWQiOiJUZXN0In1dfQ.FtbjiRU51Uf-aMPMtAkDt4DJN5BuunmzctgimQ-DPNg";
}