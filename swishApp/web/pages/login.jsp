<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/base.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/components.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/layout.css">
        <style>
            .login-card {
                background: rgba(28,28,28,0.9);
                border: 1px solid rgba(255,255,255,0.08);
                border-radius: 20px;
                padding: 2rem;
                margin-top: 3rem;
            }
            .logo {
                font-size: 3rem;
                text-align: center;
            }
            .titulo {
                color: #f47b25;
                font-weight: 800;
                text-align: center;
                font-size: 1.8rem;
            }
            .subtitulo {
                color: #aaa;
                text-align: center;
                font-size: 0.9rem;
            }
            input.form-control {
                background: rgba(255,255,255,0.08) !important;
                border: 1px solid rgba(255,255,255,0.1) !important;
                color: white !important;
                border-radius: 10px;
                padding: 0.75rem;
            }
            input.form-control:focus {
                border-color: #f47b25 !important;
                box-shadow: 0 0 0 2px rgba(244,123,37,0.3) !important;
            }
            .btn-login {
                background: #f47b25;
                color: white;
                border: none;
                border-radius: 12px;
                padding: 0.75rem;
                font-weight: bold;
                width: 100%;
                font-size: 1rem;
            }
            .btn-login:hover {
                background: #ff8a3d;
            }
            .link-registro {
                color: #f47b25;
                text-align: center;
                display: block;
                margin-top: 1rem;
                text-decoration: none;
            }
            .link-registro:hover {
                color: #ff8a3d;
            }
        </style>
    </head>
    <body>
        <div class="container" style="max-width:400px;">
            <div class="login-card">

                <div class="logo">🏀</div>
                <h2 class="titulo">Swish App</h2>
                <p class="subtitulo">Gestión de Torneos</p>

                <% if (request.getAttribute("error") != null) {%>
                <div class="alert alert-danger mt-3">
                    <%= request.getAttribute("error")%>
                </div>
                <% }%>

                <form action="<%= request.getContextPath()%>/LoginServlet"
                      method="post" class="mt-4">

                    <input type="hidden" name="accion" value="login">

                    <div class="mb-3">
                        <label class="form-label">Usuario</label>
                        <input type="text" name="username"
                               class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Contraseña</label>
                        <input type="password" name="password"
                               class="form-control" required>
                    </div>

                    <button type="submit" class="btn-login">
                        Entrar
                    </button>

                </form>

                <a href="<%= request.getContextPath()%>/pages/registro.jsp"
                   class="link-registro">
                    ¿No tienes cuenta? Regístrate
                </a>

            </div>
        </div>
    </body>
</html>