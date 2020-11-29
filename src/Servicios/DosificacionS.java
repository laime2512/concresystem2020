package Servicios;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import Modelos.Dosificacion;
import Modelos.Factura;
import Wrap.ClienteNit;
import Wrap.FacturaDosificacionWrap;
import pagination.DataTableResults;

public interface DosificacionS {
	DataTableResults<Dosificacion> listar(HttpServletRequest request);
	Dosificacion obtener(Integer cod);
	Factura obtenerfacturaxventa(Long codven);
	Dosificacion dosificacionactual(Integer codsuc);
	Dosificacion dosificacionUltimaDosificacion(Integer codsuc);
	int adicionar(Dosificacion obj);
	boolean modificar(Dosificacion obj);
	boolean eliminar(Integer cod);
	boolean finalizar(Integer cod);
	boolean anularFactura(Long codven);
	DataTableResults<Factura> listarFacturas(HttpServletRequest request,String estado,Integer dosificacion);
	DataTableResults<ClienteNit> listarNit(HttpServletRequest request);
	String buscarClientexnit(String nit);
	int adicionarFactura(Factura f);
	int obtenerNumFac(Integer cod);
	Dosificacion getLastData();
	List<FacturaDosificacionWrap> listFacturaDosificacion(String fini, String ffin, Integer codsuc);
}
