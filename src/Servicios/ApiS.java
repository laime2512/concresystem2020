package Servicios;

import java.util.List;
import java.util.Map;

import Modelos.Producto;
import Modelos.UserWebService;
import Modelos.Venta;

public interface ApiS {
	public List<Producto> lista_productos(String cat);
	public Map<String, Object> iniciar_sesion(String log,String cla);
	public List<Venta> lista_ventas(Long codusu) ;
	public List<UserWebService> lista_usuarios() ;
	public String adicionar_producto(Producto obj);
}
