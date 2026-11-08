@param kh.edu.istad.platform.authorization.web.i18n.I18nHelper msg = null
@param String currentLang = "en"
@param String telegramBotUsername = null
@param String error = null
@param String logout = null
@param String registered = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout(
    title = (msg != null ? msg.get("login.title") : "Sign in"),
    msg = msg,
    currentLang = currentLang,
    content = @`
        <div class="auth-container">
            <div class="card">
                <div style="text-align: center; margin-bottom: 1.75rem;">
                    <h1 style="font-size: 1.6rem; font-weight: 700; margin-bottom: 0.5rem;">${msg != null ? msg.get("login.title") : "Sign in"}</h1>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">${msg != null ? msg.get("login.subtitle") : "Sign in to your account"}</p>
                </div>

                @if(error != null)
                    <div class="alert alert-error">
                        ${msg != null ? msg.get("login.error") : "Invalid username or password"}
                    </div>
                @endif

                @if(logout != null)
                    <div class="alert alert-success">
                        ${msg != null ? msg.get("login.logoutSuccess") : "You have been signed out"}
                    </div>
                @endif

                @if(registered != null)
                    <div class="alert alert-success">
                        ${msg != null ? msg.get("register.success") : "Account created successfully! Please sign in."}
                    </div>
                @endif

                <form action="/login" method="post">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                    @endif

                    <div class="form-group">
                        <label class="form-label" for="username">${msg != null ? msg.get("login.username") : "Username"}</label>
                        <input type="text" id="username" name="username" class="form-control" placeholder="${msg != null ? msg.get("login.usernamePlaceholder") : "Enter username"}" required autofocus/>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="password">${msg != null ? msg.get("login.password") : "Password"}</label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="${msg != null ? msg.get("login.passwordPlaceholder") : "Enter password"}" required/>
                    </div>

                    <button type="submit" class="btn btn-primary btn-block" style="margin-top: 0.5rem;">
                        ${msg != null ? msg.get("login.signInButton") : "Sign In"}
                    </button>
                </form>

                <div class="divider">
                    <span>${msg != null ? msg.get("login.orContinueWith") : "Or continue with"}</span>
                </div>

                <div style="display: flex; flex-direction: column; gap: 0.65rem;">
                    <a href="/oauth2/authorization/google" class="btn btn-secondary btn-block">
                        <svg width="18" height="18" viewBox="0 0 24 24"><path fill="#EA4335" d="M12 5c1.6 0 3 .6 4.1 1.6l3.1-3.1C17.3 1.8 14.8 1 12 1 7.5 1 3.7 3.6 1.9 7.3l3.7 2.9C6.5 7.4 9 5 12 5z"/><path fill="#4285F4" d="M23.5 12.3c0-.8-.1-1.7-.2-2.3H12v4.5h6.5c-.3 1.5-1.1 2.8-2.4 3.7l3.7 2.9c2.2-2 3.7-5 3.7-8.8z"/><path fill="#FBBC05" d="M5.6 14.8c-.2-.7-.4-1.5-.4-2.3 0-.8.1-1.6.4-2.3L1.9 7.3C.7 9.7 0 12.3 0 15.2c0 2.9.7 5.5 1.9 7.9l3.7-2.9z"/><path fill="#34A853" d="M12 23.5c3.2 0 6-1.1 8-3l-3.7-2.9c-1.1.7-2.5 1.2-4.3 1.2-3 0-5.5-2.4-6.4-5.2L1.9 16.5C3.7 20.2 7.5 23.5 12 23.5z"/></svg>
                        <span>${msg != null ? msg.get("login.continueWith", "Google") : "Google"}</span>
                    </a>

                    <a href="/oauth2/authorization/github" class="btn btn-secondary btn-block">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.53 1.032 1.53 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"/></svg>
                        <span>${msg != null ? msg.get("login.continueWith", "GitHub") : "GitHub"}</span>
                    </a>

                    <a href="/oauth2/authorization/facebook" class="btn btn-secondary btn-block">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="#1877F2"><path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/></svg>
                        <span>${msg != null ? msg.get("login.continueWith", "Facebook") : "Facebook"}</span>
                    </a>

                    @if(telegramBotUsername != null && !telegramBotUsername.isBlank())
                        <div style="display: flex; justify-content: center; margin-top: 0.5rem;">
                            <script async src="https://telegram.org/js/telegram-widget.js?22"
                                    data-telegram-login="${telegramBotUsername}"
                                    data-size="large"
                                    data-radius="8"
                                    data-auth-url="/callback/telegram"
                                    data-request-access="write"></script>
                        </div>
                    @endif
                </div>

                <div style="text-align: center; margin-top: 1.5rem; font-size: 0.875rem; color: var(--text-muted);">
                    <span>${msg != null ? msg.get("login.noAccount") : "Don't have an account?"}</span>
                    <a href="/register" style="color: var(--primary); text-decoration: none; font-weight: 600; margin-left: 0.35rem;">
                        ${msg != null ? msg.get("login.createAccount") : "Sign up"}
                    </a>
                </div>
            </div>
        </div>
    `
)

