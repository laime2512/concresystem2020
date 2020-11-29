package Servicios;

import java.util.List;
import java.util.Map;

import Modelos.Cliente;
import Modelos.Usuario;

public interface ClienteS {
	List<Map<String, Object>> listar(int start,int estado,String search,int length);
	List<Map<String, Object>> buscarproducto(String searchp,int searchc);
	List<Map<String, Object>> listarcategoria();
	List<Map<String, Object>> listar();
	Map<String, Object> obtener(Long codusu);
	Cliente obtenerCliente(Long codusu);
	String obtenerFactura(String nit);
	boolean adicionar(Cliente c,Usuario p,String log,String pass);
	void modificar(Cliente c,Usuario p,String log,String pass);
	Long generarCodigo();
	Long generarCodigoCliente();
	boolean eliminar(Long codcli);
	boolean darestado(Long codcli, int estado);
}
