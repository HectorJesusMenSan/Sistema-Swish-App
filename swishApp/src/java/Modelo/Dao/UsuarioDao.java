package Modelo.Dao;

import Configuracion.Conexion;
import Configuracion.Contrasenia;
import Modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDao {

    // =====================================================
    // REGISTRAR
    // =====================================================
    public boolean registrar(Usuario u) {
        if (existeUsername(u.getUsername())) return false;

        String sql =
            "INSERT INTO usuario(nombre, username, password) " +
            "VALUES(?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String hash = Contrasenia.hashPassword(u.getPassword());

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsername());
            ps.setString(3, hash);
            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =====================================================
    // LOGIN
    // =====================================================
    public Usuario login(String username, String password) {

        String sql =
            "SELECT * FROM usuario WHERE username = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("password");

                if (Contrasenia.verificarPassword(password, hash)) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setUsername(rs.getString("username"));
                    return u;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // EXISTE USERNAME
    // =====================================================
    public boolean existeUsername(String username) {

        String sql =
            "SELECT COUNT(*) FROM usuario WHERE username = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1) > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}