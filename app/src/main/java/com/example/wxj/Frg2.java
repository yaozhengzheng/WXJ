package com.example.wxj;

import android.app.Fragment;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Created on 2017/10/9.
 * author ${yao}.
 */

public class Frg2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg2, container, false);
        WebView wv = view.findViewById(R.id.web);
        WebSettings settings = wv.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                if (error.getPrimaryError() == SslError.SSL_DATE_INVALID
                        || error.getPrimaryError() == SslError.SSL_EXPIRED
                        || error.getPrimaryError() == SslError.SSL_INVALID
                        || error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
                    if (chkMySSLCNCert(error.getCertificate())) {
                        handler.proceed();  // 如果证书一致，忽略错误
                    } else {
                        handler.cancel();
                    }
                    super.onReceivedSslError(view, handler, error);
                }
            }
        });
        wv.loadUrl("https://robot.infinitus.com.cn/robot/skin/infinitus/wxCardNum.html");
        return view;
    }

    private boolean chkMySSLCNCert(SslCertificate cert) {
        byte[] MySSLCNSHA256 = {35, 76, 110, -121, -68, -104, -12, 84, 39, 119, -55,
                101, 95, -8, -90, 9, 36, -108, 5, -57, 76, -98, -19, -73, 91, -37, 18,
                64, 32, -41, 0, 109};  //证书指纹
        Bundle bundle = SslCertificate.saveState(cert);
        byte[] bytes = bundle.getByteArray("x509-certificate");
        if (bytes != null) {
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Certificate ca = cf.generateCertificate(new ByteArrayInputStream(bytes));
                MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                byte[] Key = sha256.digest(((X509Certificate) ca).getEncoded());
                return Arrays.equals(Key, MySSLCNSHA256);
            } catch (Exception Ex) {
            }
        }
        return false;
    }

    public static Frg2 newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("b", content);
        Frg2 fragment = new Frg2();
        fragment.setArguments(args);
        return fragment;
    }
}
