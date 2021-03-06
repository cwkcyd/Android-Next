package com.mcxiaoke.next.http;

import android.net.Uri;
import android.net.Uri.Builder;
import com.mcxiaoke.next.http.util.URIBuilder;
import org.apache.http.NameValuePair;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collection;

/**
 * User: mcxiaoke
 * Date: 14-5-30
 * Time: 15:09
 */
final class Utils {

    public static boolean hasOkHttp() {
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            Class.forName("com.squareup.okhttp.OkUrlFactory");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isSuccess(int code) {
        return code >= HttpURLConnection.HTTP_OK && code < HttpURLConnection.HTTP_MULT_CHOICE;
    }

    public static boolean isRedirect(int code) {
        return code >= HttpURLConnection.HTTP_MULT_CHOICE && code < HttpURLConnection.HTTP_BAD_REQUEST;
    }

    public static URL toURL(String uriString) {
        try {
            return new URL(uriString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL: " + uriString);
        }
    }

    public static HostnameVerifier createTrustAllHostNameVerifier() {
        return new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }


    public static SSLSocketFactory createTrustedAllSslSocketFactory() {
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                // Intentionally left blank
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                // Intentionally left blank
            }
        }};
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, new SecureRandom());
            return context.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(
                    "Security exception configuring SSL context", e);
        }
    }


    public static String appendQuery(String url, Collection<NameValuePair> params) {
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.addParameters(params);
            return builder.toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("URI Syntax error:" + url, e);
        }
    }

    public static String appendQuery2(String url, Collection<NameValuePair> params) {
        Builder builder = Uri.parse(url).buildUpon();
        for (NameValuePair pair : params) {
            builder.appendQueryParameter(pair.getName(), pair.getValue());
        }
        return builder.toString();
    }
}
