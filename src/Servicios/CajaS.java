package Servicios;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import Modelos.Caja;
import Modelos.DetalleCaja;
import pagination.DataTableResults;

public interface CajaS {
	DataTableResults<Caja> lista(HttpServletRequest request);
	DataTableResults<DetalleCaja> listarDetalles(HttpServletRequest request,Long codcaja);
	Caja obtener(Caja obj) throws SQLException ;
	Long adicionar(Caja obj) ;
	Long adicionarDetalle(DetalleCaja det);
	boolean eliminar(Caja obj) throws SQLException;
	boolean finalizar(Caja obj)throws SQLException;
	Caja verificarUsuarioActivo(Long codusu,Integer codsuc);
	Caja getLastCajaFinallyUser(Long codusu, Integer codsuc);
}
