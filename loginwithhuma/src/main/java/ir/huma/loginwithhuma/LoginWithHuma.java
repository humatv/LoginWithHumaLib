package ir.huma.loginwithhuma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

public class LoginWithHuma {

    private final static String send = "ir.huma.android.launcher.login";
    private final static String receive = "ir.huma.android.launcher.loginResponse";

    private Context context;
    private String clientKey;
    private String scope = "phone";
    private OnLoginListener onLoginListener;

    public LoginWithHuma(Context context) {
        this.context = context;
    }

    public String getClientKey() {
        return clientKey;
    }

    public LoginWithHuma setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public OnLoginListener getOnLoginListener() {
        return onLoginListener;
    }

    public LoginWithHuma setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public LoginWithHuma setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public void send() {
        if (clientKey == null || clientKey.equals("")) {
            throw new RuntimeException("please set Client Key in java code!!!");
        }
        if (onLoginListener == null) {
            throw new RuntimeException("please setOnLoginListener in java code!!!");
        }
        if (!isHumaPlatform()) {
            Toast.makeText(context, "لطفا ابتدا برنامه هوما استور را نصب کنید.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (scope == null || scope.equals("")) {
            throw new RuntimeException("Scope is not be Null!!!");
        }

        try {
            sendLoginToStore();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                sendLoginToProfile();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void sendLoginToStore() {
        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("app://login.huma.ir"));
        in.setPackage("ir.huma.humastore");
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        in.putExtra("key", clientKey);
        in.putExtra("package", getContext().getPackageName());
        in.putExtra("scope", scope);
        getContext().startActivity(in);
        getContext().registerReceiver(receiver, new IntentFilter(receive));
    }

    private void sendLoginToProfile() {
        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("app://wizard.huma.ir"));
        in.setPackage("ir.huma.humawizard");
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        in.putExtra("key", clientKey);
        in.putExtra("package", getContext().getPackageName());
        in.putExtra("scope", scope);
        getContext().startActivity(in);
        getContext().registerReceiver(receiver, new IntentFilter(receive));
    }


    public void unregiter() {
        try {
            getContext().unregisterReceiver(receiver);
        } catch (Exception e) {

        }
    }


    public boolean isHumaPlatform() {
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo("ir.huma.humastore", 0);
            if(packageInfo.versionCode > 44){
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            return false;
        }

        try {
            getContext().getPackageManager().getPackageInfo("ir.huma.humawizard", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            return false;
        }

    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (onLoginListener != null) {
                    if (intent.hasExtra("packageName") && intent.getStringExtra("packageName").equals(getContext().getPackageName())) {
                        if (intent.getBooleanExtra("success", false)) {
                            onLoginListener.onLogin(intent.getStringExtra("message"));
                        } else {
                            onLoginListener.onFail(intent.getStringExtra("message"));
                        }
                        try {
                            getContext().unregisterReceiver(receiver);
                        } catch (Exception e) {

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };



    public interface OnLoginListener {
        void onLogin(String code);

        void onFail(String message);
    }


}
