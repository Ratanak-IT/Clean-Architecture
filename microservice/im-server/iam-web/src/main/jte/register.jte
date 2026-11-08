@param kh.edu.istad.platform.authorization.web.i18n.I18nHelper msg = null
@param String currentLang = "en"
@param kh.edu.istad.platform.authorization.web.dto.RegisterWebForm form = null
@param String errorMessage = null
@param org.springframework.validation.BindingResult errors = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout(
    title = (msg != null ? msg.get("register.title") : "Create an account"),
    msg = msg,
    currentLang = currentLang,
    content = @`
        <div class="auth-container" style="max-width: 500px;">
            <div class="card">
                <div style="text-align: center; margin-bottom: 1.75rem;">
                    <h1 style="font-size: 1.6rem; font-weight: 700; margin-bottom: 0.5rem;">${msg != null ? msg.get("register.title") : "Create an account"}</h1>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">${msg != null ? msg.get("register.subtitle") : "Sign up for a new account"}</p>
                </div>

                @if(errorMessage != null)
                    <div class="alert alert-error">
                        ${errorMessage}
                    </div>
                @endif

                <form action="/register" method="post">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                    @endif

                    <div class="form-group">
                        <label class="form-label" for="username">${msg != null ? msg.get("register.username") : "Username"}</label>
                        <input type="text" id="username" name="username" class="form-control" value="${form != null ? form.username() : ""}" required autofocus/>
                        @if(errors != null && errors.hasFieldErrors("username"))
                            <div class="form-error">${errors.getFieldError("username").getDefaultMessage()}</div>
                        @endif
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="email">${msg != null ? msg.get("register.email") : "Email"}</label>
                        <input type="email" id="email" name="email" class="form-control" value="${form != null ? form.email() : ""}" required/>
                        @if(errors != null && errors.hasFieldErrors("email"))
                            <div class="form-error">${errors.getFieldError("email").getDefaultMessage()}</div>
                        @endif
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label" for="firstName">${msg != null ? msg.get("register.givenName") : "First Name"}</label>
                            <input type="text" id="firstName" name="firstName" class="form-control" value="${form != null ? form.firstName() : ""}"/>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="lastName">${msg != null ? msg.get("register.familyName") : "Last Name"}</label>
                            <input type="text" id="lastName" name="lastName" class="form-control" value="${form != null ? form.lastName() : ""}"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="password">${msg != null ? msg.get("register.password") : "Password"}</label>
                        <input type="password" id="password" name="password" class="form-control" required/>
                        @if(errors != null && errors.hasFieldErrors("password"))
                            <div class="form-error">${errors.getFieldError("password").getDefaultMessage()}</div>
                        @endif
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="confirmPassword">${msg != null ? msg.get("register.confirmPassword") : "Confirm Password"}</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required/>
                        @if(errors != null && errors.hasFieldErrors("confirmPassword"))
                            <div class="form-error">${errors.getFieldError("confirmPassword").getDefaultMessage()}</div>
                        @endif
                    </div>

                    <button type="submit" class="btn btn-primary btn-block" style="margin-top: 1rem;">
                        ${msg != null ? msg.get("register.submitButton") : "Create Account"}
                    </button>
                </form>

                <div style="text-align: center; margin-top: 1.5rem; font-size: 0.875rem; color: var(--text-muted);">
                    <span>${msg != null ? msg.get("register.haveAccount") : "Already have an account?"}</span>
                    <a href="/login" style="color: var(--primary); text-decoration: none; font-weight: 600; margin-left: 0.35rem;">
                        ${msg != null ? msg.get("register.signIn") : "Sign in here"}
                    </a>
                </div>
            </div>
        </div>
    `
)

