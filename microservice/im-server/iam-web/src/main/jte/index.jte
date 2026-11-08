@param kh.edu.istad.platform.authorization.web.i18n.I18nHelper msg = null
@param String currentLang = "en"
@param kh.edu.istad.platform.authorization.domain.dto.user.result.UserResult user = null
@param java.util.Collection<String> roles = null
@param java.util.Collection<String> permissions = null
@param java.util.Map<String, Object> claims = null
@param String username = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

@template.layout(
    title = (msg != null ? msg.get("profile.title") : "My Profile"),
    msg = msg,
    currentLang = currentLang,
    username = username,
    activeTab = "index",
    _csrf = _csrf,
    content = @`
        <div>
            <!-- Navigation Tabs -->
            <div class="tabs">
                <a href="/" class="tab-item active">${msg != null ? msg.get("profile.title") : "Overview"}</a>
                <a href="/profile/edit" class="tab-item">${msg != null ? msg.get("profile.editProfile") : "Edit Profile"}</a>
                <a href="/profile/password" class="tab-item">${msg != null ? msg.get("profile.changePassword") : "Change Password"}</a>
                <a href="/profile/accounts" class="tab-item">${msg != null ? msg.get("profile.linkedAccounts") : "Linked Accounts"}</a>
                <a href="/profile/apps" class="tab-item">${msg != null ? msg.get("profile.connectedApps") : "Connected Apps"}</a>
            </div>

            <!-- Profile Overview Header -->
            <div class="card" style="margin-bottom: 1.5rem; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem;">
                <div style="display: flex; align-items: center; gap: 1.25rem;">
                    <div style="width: 64px; height: 64px; border-radius: 50%; background: linear-gradient(135deg, #6366f1, #a855f7); display: flex; align-items: center; justify-content: center; font-size: 1.5rem; font-weight: 700; color: #fff;">
                        ${(user != null && user.username() != null && !user.username().isEmpty()) ? user.username().substring(0, 1).toUpperCase() : (username != null && !username.isEmpty() ? username.substring(0, 1).toUpperCase() : "U")}
                    </div>
                    <div>
                        <h2 style="font-size: 1.35rem; font-weight: 700;">
                            ${user != null && user.name() != null && !user.name().isBlank() ? user.name() : (username != null ? username : "User")}
                        </h2>
                        <div style="color: var(--text-muted); font-size: 0.875rem; margin-top: 0.15rem;">
                            @${user != null ? user.username() : username}
                            @if(user != null && user.email() != null)
                                &bull; ${user.email()}
                            @endif
                        </div>
                    </div>
                </div>

                <div style="display: flex; gap: 0.5rem;">
                    <a href="/profile/edit" class="btn btn-secondary" style="font-size: 0.85rem;">
                        ${msg != null ? msg.get("profile.editProfile") : "Edit Profile"}
                    </a>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;">
                <!-- Roles Card -->
                <div class="card">
                    <h3 style="font-size: 1.05rem; font-weight: 600; margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
                        <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/></svg>
                        ${msg != null ? msg.get("profile.roles") : "Assigned Roles"}
                    </h3>

                    <div style="display: flex; flex-wrap: wrap; gap: 0.5rem;">
                        @if(roles != null && !roles.isEmpty())
                            @for(var role : roles)
                                <span class="badge badge-primary" style="font-size: 0.85rem; padding: 0.35rem 0.75rem;">
                                    ${role}
                                </span>
                            @endfor
                        @elseif(user != null && user.roles() != null && !user.roles().isEmpty())
                            @for(var role : user.roles())
                                <span class="badge badge-primary" style="font-size: 0.85rem; padding: 0.35rem 0.75rem;">
                                    ${role.name()}
                                </span>
                            @endfor
                        @else
                            <p style="color: var(--text-dim); font-size: 0.875rem;">${msg != null ? msg.get("profile.noRoles") : "No roles assigned"}</p>
                        @endif
                    </div>
                </div>

                <!-- Effective Permissions Card -->
                <div class="card">
                    <h3 style="font-size: 1.05rem; font-weight: 600; margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
                        <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/></svg>
                        ${msg != null ? msg.get("profile.permissions") : "Effective Permissions"}
                    </h3>

                    <div style="display: flex; flex-wrap: wrap; gap: 0.5rem; max-height: 200px; overflow-y: auto;">
                        @if(permissions != null && !permissions.isEmpty())
                            @for(var perm : permissions)
                                <span class="badge" style="font-size: 0.8rem; padding: 0.3rem 0.65rem; background: #e0f2fe; color: #0369a1; border-color: #bae6fd;">
                                    ${perm}
                                </span>
                            @endfor
                        @else
                            <p style="color: var(--text-dim); font-size: 0.875rem;">${msg != null ? msg.get("profile.noPermissions") : "No permissions granted"}</p>
                        @endif
                    </div>
                </div>
            </div>

            <!-- Claims & Account Details Card -->
            <div class="card" style="margin-top: 1.5rem;">
                <h3 style="font-size: 1.05rem; font-weight: 600; margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
                    <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4"/></svg>
                    ${msg != null ? msg.get("profile.details") : "Personal Details & Claims"}
                </h3>

                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1rem;">
                    <div style="background: #f8fafc; padding: 0.85rem; border-radius: 8px; border: 1px solid var(--border-color);">
                        <div style="color: var(--text-dim); font-size: 0.75rem; text-transform: uppercase; font-weight: 600;">${msg != null ? msg.get("profile.username") : "Username"}</div>
                        <div style="color: var(--text-main); font-weight: 600; margin-top: 0.25rem;">${user != null ? user.username() : username}</div>
                    </div>

                    <div style="background: #f8fafc; padding: 0.85rem; border-radius: 8px; border: 1px solid var(--border-color);">
                        <div style="color: var(--text-dim); font-size: 0.75rem; text-transform: uppercase; font-weight: 600;">${msg != null ? msg.get("profile.email") : "Email"}</div>
                        <div style="color: var(--text-main); font-weight: 600; margin-top: 0.25rem;">${user != null && user.email() != null ? user.email() : "—"}</div>
                    </div>

                    <div style="background: #f8fafc; padding: 0.85rem; border-radius: 8px; border: 1px solid var(--border-color);">
                        <div style="color: var(--text-dim); font-size: 0.75rem; text-transform: uppercase; font-weight: 600;">${msg != null ? msg.get("profile.fullName") : "Full Name"}</div>
                        <div style="color: var(--text-main); font-weight: 600; margin-top: 0.25rem;">${user != null && user.name() != null ? user.name() : "—"}</div>
                    </div>

                    <div style="background: #f8fafc; padding: 0.85rem; border-radius: 8px; border: 1px solid var(--border-color);">
                        <div style="color: var(--text-dim); font-size: 0.75rem; text-transform: uppercase; font-weight: 600;">${msg != null ? msg.get("common.status") : "Status"}</div>
                        <div style="color: var(--text-main); font-weight: 600; margin-top: 0.25rem;">
                            @if(user != null && user.enabled())
                                <span class="badge badge-success">${msg != null ? msg.get("common.active") : "Active"}</span>
                            @else
                                <span class="badge">${msg != null ? msg.get("common.inactive") : "Inactive"}</span>
                            @endif
                        </div>
                    </div>
                </div>

                @if(claims != null && !claims.isEmpty())
                    <div style="margin-top: 1.5rem;">
                        <h4 style="font-size: 0.9rem; font-weight: 600; color: var(--text-muted); margin-bottom: 0.75rem;">OIDC / Authentication Claims</h4>
                        <div style="background: #f8fafc; border: 1px solid var(--border-color); border-radius: 8px; overflow-x: auto;">
                            <table style="width: 100%; border-collapse: collapse; font-size: 0.85rem;">
                                <tbody>
                                    @for(var entry : claims.entrySet())
                                        <tr style="border-bottom: 1px solid #e2e8f0;">
                                            <td style="padding: 0.6rem 1rem; color: #64748b; font-family: monospace; font-weight: 600; width: 35%;">${entry.getKey()}</td>
                                            <td style="padding: 0.6rem 1rem; color: #0f172a; font-family: monospace;">${String.valueOf(entry.getValue())}</td>
                                        </tr>
                                    @endfor
                                </tbody>
                            </table>
                        </div>
                    </div>
                @endif
            </div>
        </div>
    `
)
