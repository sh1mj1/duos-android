package com.example.duos.ui.login;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u00032\u00020\u0004B\u0005\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0014J\b\u0010\b\u001a\u00020\u0007H\u0002J\u0012\u0010\t\u001a\u00020\u00072\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016J\u0018\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0007H\u0016J\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0014H\u0016\u00a8\u0006\u0015"}, d2 = {"Lcom/example/duos/ui/login/LoginActivity;", "Lcom/example/duos/ui/BaseActivity;", "Lcom/example/duos/databinding/ActivityLoginBinding;", "Lcom/example/duos/ui/login/LoginView;", "Landroid/view/View$OnClickListener;", "()V", "initAfterBinding", "", "login", "onClick", "v", "Landroid/view/View;", "onLoginFailure", "code", "", "message", "", "onLoginLoading", "onLoginSuccess", "auth", "Lcom/example/duos/data/remote/auth/Auth;", "app_debug"})
public final class LoginActivity extends com.example.duos.ui.BaseActivity<com.example.duos.databinding.ActivityLoginBinding> implements com.example.duos.ui.login.LoginView, android.view.View.OnClickListener {
    
    public LoginActivity() {
        super(null);
    }
    
    @java.lang.Override()
    protected void initAfterBinding() {
    }
    
    @java.lang.Override()
    public void onClick(@org.jetbrains.annotations.Nullable()
    android.view.View v) {
    }
    
    private final void login() {
    }
    
    @java.lang.Override()
    public void onLoginLoading() {
    }
    
    @java.lang.Override()
    public void onLoginSuccess(@org.jetbrains.annotations.NotNull()
    com.example.duos.data.remote.auth.Auth auth) {
    }
    
    @java.lang.Override()
    public void onLoginFailure(int code, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
}