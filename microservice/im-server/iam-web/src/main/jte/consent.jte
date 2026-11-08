@param kh.edu.istad.platform.authorization.web.i18n.I18nHelper msg = null
@param String currentLang = "en"
@param String clientId = null
@param String clientName = null
@param String state = null
@param java.util.Set<kh.edu.istad.platform.authorization.web.service.ConsentWebService.ScopeWithDescription> scopes = null
@param java.util.Set<kh.edu.istad.platform.authorization.web.service.ConsentWebService.ScopeWithDescription> previouslyApprovedScopes = null
@param String principalName = null
@param String userCode = null
@param String requestURI = "/oauth2/authorize"
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout(
    title = (msg != null ? msg.get("consent.title") : "Authorization Request"),
    msg = msg,
    currentLang = currentLang,
    username = principalName,
    content = @`
        <div class="auth-container" style="max-width: 520px;">
            <div class="card">
                <div style="text-align: center; margin-bottom: 1.75rem;">
                    <div style="width: 56px; height: 56px; border-radius: 16px; background: #eef2ff; border: 1px solid #c7d2fe; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 1rem; color: #4f46e5;">
                        <svg width="28" height="28" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/></svg>
                    </div>
                    <h1 style="font-size: 1.4rem; font-weight: 700; margin-bottom: 0.35rem;">${clientName != null ? clientName : clientId}</h1>
                    <p style="color: var(--text-muted); font-size: 0.9rem;">${msg != null ? msg.get("consent.subtitle") : "wants to access your account"}</p>
                </div>

                <form method="post" action="${requestURI}">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                    @endif
                    <input type="hidden" name="client_id" value="${clientId}"/>
                    <input type="hidden" name="state" value="${state}"/>
                    @if(userCode != null)
                        <input type="hidden" name="user_code" value="${userCode}"/>
                    @endif

                    <div style="margin-bottom: 1.5rem;">
                        <h3 style="font-size: 0.9rem; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-muted); margin-bottom: 0.75rem;">
                            ${msg != null ? msg.get("consent.requestedScopes") : "Requested Permissions"}
                        </h3>

                        <div style="display: flex; flex-direction: column; gap: 0.65rem;">
                            @if(scopes != null && !scopes.isEmpty())
                                @for(var scope : scopes)
                                    <label style="display: flex; align-items: flex-start; gap: 0.75rem; padding: 0.75rem; background: #f8fafc; border: 1px solid var(--border-color); border-radius: 8px; cursor: pointer;">
                                        <input type="checkbox" name="scope" value="${scope.scope()}" checked style="margin-top: 0.2rem; accent-color: var(--primary);"/>
                                        <div>
                                            <div style="font-weight: 600; font-size: 0.9rem; color: var(--text-main);">
                                                ${scope.scope()}
                                            </div>
                                            <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 0.15rem;">
                                                ${msg != null ? msg.get("consent.scope." + scope.scope(), scope.description()) : scope.description()}
                                            </div>
                                        </div>
                                    </label>
                                @endfor
                            @endif

                            @if(previouslyApprovedScopes != null && !previouslyApprovedScopes.isEmpty())
                                @for(var scope : previouslyApprovedScopes)
                                    <label style="display: flex; align-items: flex-start; gap: 0.75rem; padding: 0.75rem; background: #f8fafc; border: 1px solid var(--border-color); border-radius: 8px; opacity: 0.85;">
                                        <input type="checkbox" name="scope" value="${scope.scope()}" checked disabled style="margin-top: 0.2rem;"/>
                                        <div>
                                            <div style="font-weight: 600; font-size: 0.9rem; color: var(--text-main);">
                                                ${scope.scope()}
                                                <span class="badge badge-success" style="margin-left: 0.5rem; font-size: 0.7rem;">Approved</span>
                                            </div>
                                            <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 0.15rem;">
                                                ${msg != null ? msg.get("consent.scope." + scope.scope(), scope.description()) : scope.description()}
                                            </div>
                                        </div>
                                    </label>
                                @endfor
                            @endif
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0.75rem; margin-top: 1.5rem;">
                        <button type="submit" name="consent_action" value="cancel" class="btn btn-secondary" onclick="document.getElementById('consent-cancel-form').submit(); return false;">
                            ${msg != null ? msg.get("consent.cancel") : "Cancel"}
                        </button>
                        <button type="submit" class="btn btn-primary">
                            ${msg != null ? msg.get("consent.allow") : "Allow"}
                        </button>
                    </div>
                </form>

                <form id="consent-cancel-form" method="post" action="${requestURI}" style="display: none;">
                    @if(_csrf != null)
                        <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                    @endif
                    <input type="hidden" name="client_id" value="${clientId}"/>
                    <input type="hidden" name="state" value="${state}"/>
                    @if(userCode != null)
                        <input type="hidden" name="user_code" value="${userCode}"/>
                    @endif
                    <input type="hidden" name="cancel" value="true"/>
                </form>
            </div>
        </div>
    `
)

