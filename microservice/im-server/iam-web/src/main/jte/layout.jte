@param String title
@param gg.jte.Content content
@param kh.edu.istad.platform.authorization.web.i18n.I18nHelper msg = null
@param String currentLang = "en"
@param String username = null
@param String activeTab = ""
@param org.springframework.security.web.csrf.CsrfToken _csrf = null

<!DOCTYPE html>
<html lang="${currentLang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title != null ? title : (msg != null ? msg.get("common.appName") : "ISTAD IAM")}</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&family=Kantumruy+Pro:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg-body: #f8fafc;
            --bg-card: #ffffff;
            --bg-card-hover: #f1f5f9;
            --border-color: #e2e8f0;
            --border-focus: #4f46e5;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --text-dim: #94a3b8;
            --primary: #4f46e5;
            --primary-hover: #4338ca;
            --primary-light: #eef2ff;
            --danger: #ef4444;
            --danger-hover: #dc2626;
            --success: #10b981;
            --warning: #f59e0b;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Plus Jakarta Sans', 'Kantumruy Pro', -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
            background-color: var(--bg-body);
            color: var(--text-main);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            line-height: 1.5;
        }

        header {
            border-bottom: 1px solid var(--border-color);
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(12px);
            position: sticky;
            top: 0;
            z-index: 50;
        }

        .nav-container {
            max-width: 1100px;
            margin: 0 auto;
            padding: 0.85rem 1.5rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 0.75rem;
            text-decoration: none;
            color: var(--text-main);
            font-weight: 700;
            font-size: 1.15rem;
        }

        .brand-icon {
            width: 32px;
            height: 32px;
            border-radius: 8px;
            background: linear-gradient(135deg, #4f46e5, #7c3aed);
            display: flex;
            align-items: center;
            justify-content: center;
            color: #fff;
            font-weight: bold;
        }

        .nav-right {
            display: flex;
            align-items: center;
            gap: 1.25rem;
        }

        .lang-switch {
            display: flex;
            align-items: center;
            background: #f1f5f9;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            padding: 3px;
            font-size: 0.825rem;
            font-weight: 500;
        }

        .lang-switch a {
            padding: 4px 10px;
            border-radius: 6px;
            text-decoration: none;
            color: var(--text-muted);
            transition: all 0.2s;
        }

        .lang-switch a.active {
            background: var(--primary);
            color: #ffffff;
            font-weight: 600;
        }

        .main-wrapper {
            flex: 1;
            max-width: 1100px;
            width: 100%;
            margin: 0 auto;
            padding: 2rem 1.5rem 3rem 1.5rem;
        }

        .card {
            background: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 14px;
            padding: 2rem;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -2px rgba(0, 0, 0, 0.05);
        }

        .auth-container {
            max-width: 440px;
            margin: 2rem auto;
            width: 100%;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 0.65rem 1.25rem;
            font-size: 0.9rem;
            font-weight: 600;
            border-radius: 8px;
            text-decoration: none;
            border: 1px solid transparent;
            cursor: pointer;
            transition: all 0.2s;
            gap: 0.5rem;
        }

        .btn-primary {
            background: var(--primary);
            color: #ffffff;
        }

        .btn-primary:hover {
            background: var(--primary-hover);
        }

        .btn-secondary {
            background: #ffffff;
            color: var(--text-main);
            border-color: var(--border-color);
        }

        .btn-secondary:hover {
            background: #f8fafc;
            border-color: #cbd5e1;
        }

        .btn-danger {
            background: #fef2f2;
            color: #ef4444;
            border-color: #fecaca;
        }

        .btn-danger:hover {
            background: var(--danger);
            color: #ffffff;
        }

        .btn-block {
            width: 100%;
        }

        .form-group {
            margin-bottom: 1.25rem;
        }

        .form-label {
            display: block;
            margin-bottom: 0.45rem;
            font-size: 0.875rem;
            font-weight: 500;
            color: var(--text-muted);
        }

        .form-control {
            width: 100%;
            padding: 0.65rem 0.9rem;
            background: #ffffff;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            color: var(--text-main);
            font-size: 0.925rem;
            outline: none;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        .form-control:focus {
            border-color: var(--border-focus);
            box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.15);
        }

        .form-error {
            color: #dc2626;
            font-size: 0.8rem;
            margin-top: 0.35rem;
        }

        .alert {
            padding: 0.85rem 1.15rem;
            border-radius: 8px;
            margin-bottom: 1.25rem;
            font-size: 0.875rem;
            display: flex;
            align-items: center;
            gap: 0.6rem;
        }

        .alert-success {
            background: #ecfdf5;
            border: 1px solid #a7f3d0;
            color: #065f46;
        }

        .alert-error {
            background: #fef2f2;
            border: 1px solid #fecaca;
            color: #991b1b;
        }

        .tabs {
            display: flex;
            gap: 0.5rem;
            border-bottom: 1px solid var(--border-color);
            margin-bottom: 2rem;
            overflow-x: auto;
        }

        .tab-item {
            padding: 0.75rem 1.25rem;
            text-decoration: none;
            color: var(--text-muted);
            font-size: 0.9rem;
            font-weight: 500;
            border-bottom: 2px solid transparent;
            margin-bottom: -1px;
            white-space: nowrap;
            transition: all 0.2s;
        }

        .tab-item:hover {
            color: var(--text-main);
        }

        .tab-item.active {
            color: var(--primary);
            border-bottom-color: var(--primary);
            font-weight: 600;
        }

        .badge {
            display: inline-flex;
            align-items: center;
            padding: 0.25rem 0.65rem;
            font-size: 0.75rem;
            font-weight: 600;
            border-radius: 9999px;
            background: #f1f5f9;
            color: #475569;
            border: 1px solid #e2e8f0;
        }

        .badge-primary {
            background: #eef2ff;
            color: #4338ca;
            border-color: #c7d2fe;
        }

        .badge-success {
            background: #ecfdf5;
            color: #065f46;
            border-color: #a7f3d0;
        }

        .divider {
            position: relative;
            text-align: center;
            margin: 1.5rem 0;
        }

        .divider::before {
            content: '';
            position: absolute;
            left: 0;
            top: 50%;
            width: 100%;
            height: 1px;
            background: var(--border-color);
        }

        .divider span {
            position: relative;
            background: var(--bg-card);
            padding: 0 0.75rem;
            color: var(--text-dim);
            font-size: 0.8rem;
        }

        footer {
            border-top: 1px solid var(--border-color);
            padding: 1.5rem;
            text-align: center;
            color: var(--text-dim);
            font-size: 0.8rem;
            background: #ffffff;
        }
    </style>
</head>
<body>
    <header>
        <div class="nav-container">
            <a href="/" class="brand">
                <div class="brand-icon">I</div>
                <span>${msg != null ? msg.get("common.appName") : "ISTAD IAM"}</span>
            </a>
            <div class="nav-right">
                <div class="lang-switch">
                    <a href="?lang=en" class="${"en".equals(currentLang) ? "active" : ""}">EN</a>
                    <a href="?lang=km" class="${"km".equals(currentLang) ? "active" : ""}">ខ្មែរ</a>
                </div>
                @if(username != null && !"anonymousUser".equals(username))
                    <form action="/logout" method="post" style="display:inline;">
                        @if(_csrf != null)
                            <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
                        @endif
                        <button type="submit" class="btn btn-secondary" style="padding: 0.4rem 0.85rem; font-size: 0.8rem;">
                            ${msg != null ? msg.get("common.logout") : "Sign out"}
                        </button>
                    </form>
                @endif
            </div>
        </div>
    </header>

    <main class="main-wrapper">
        ${content}
    </main>

    <footer>
        &copy; 2026 ISTAD Identity and Access Management System.
    </footer>
</body>
</html>

