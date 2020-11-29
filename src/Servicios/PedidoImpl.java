package Servicios;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Cliente;
import Modelos.DetallePedido;
import Modelos.Pedido;
import Modelos.Producto;
import Modelos.Usuario;
import Utiles.Constantes;
import Utiles.Fechas;

@Service
@Transactional
public class PedidoImpl implements PedidoS{
	@Autowired
	private ProductoS productoS;
	@Autowired
	private ClienteImpl clienteS;
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}
	
	public void adicionarPedidoSesion(HttpSession sesion,Usuario user) {
		Cliente cliente = clienteS.obtenerCliente(user.getCodusu());
		if(cliente !=null) {
			Pedido pedido = new Pedido();
			pedido.setCodusu(user.getCodusu());
			pedido.setDireccion(cliente.getDireccion());
			pedido.setCelular(cliente.getCelular());
			pedido.setXusuario(user.getNombre()+" "+user.getAp()+""+(user.getAm()==null ? "" : user.getAm()));
			pedido.setXfecha(Fechas.getDateNowToString("dd/MM/yy"));
			pedido.setObservacion("");
			pedido.setNit(cliente.getNit());
			pedido.setRazon_nit(cliente.getRazon_nit());
			pedido.setCliente(cliente);
			List<DetallePedido> detalleList = new ArrayList<DetallePedido>();
			sesion.setAttribute(Constantes.Sesiones.PEDIDO, pedido);
			sesion.setAttribute(Constantes.Sesiones.DETALLE, detalleList);
		}
	}
	public void adicionarDetalleSesion(HttpServletRequest request,DetallePedido detalle) {
		if(isValidDetalle(detalle)) {
			HttpSession sesion = request.getSession();
			@SuppressWarnings("unchecked")
			List<DetallePedido> detallePedidoList =  (List<DetallePedido>) sesion.getAttribute(Constantes.Sesiones.DETALLE);
			//Eliminar detalle de producto si ya existe
			eliminarDetalleExistente(detallePedidoList, detalle);
			Producto producto = productoS.obtener(detalle.getCodpro());
			detalle.setXproducto(producto.getNombre());
			detalle.setSubtotal(detalle.getCantidad()*detalle.getPrecio());
			detallePedidoList.add(detalle);
			sesion.setAttribute(Constantes.Sesiones.DETALLE, detallePedidoList);
		}
	}
	public void mostrarDetalle(List<DetallePedido> detallePedidoList) {
		if(detallePedidoList != null ) 
		if(!detallePedidoList.isEmpty())
			for (DetallePedido detallePedido : detallePedidoList) {
				System.out.println(detallePedido.toString());
			}
	}
	public Boolean isValidDetalle(DetallePedido detalle) {
		if(detalle== null)
			return false;
		if(detalle.getCodpro()== null )
			return false;
		if(detalle.getCantidad() == null || detalle.getCantidad() <=0 || detalle.getCantidad()>Constantes.Pedidos.MAX_CANTIDAD_PEDIDO)
			return false;
		if(detalle.getPrecio() == null || detalle.getPrecio()<=0)
			return false;
		return true;
	}
	public Boolean isValidDetallePedidoList(List<DetallePedido> detallePedidoList) {
		if(detallePedidoList == null ) 
			return false;
		if(detallePedidoList.isEmpty())
			return false;
		for (DetallePedido detallePedido : detallePedidoList) {
			if(!isValidDetalle(detallePedido))
				return false;
		}
		return true;
	}
	public void eliminarDetalleExistente(List<DetallePedido> detallePedidoList,DetallePedido detalle) {
		if(isValidDetallePedidoList(detallePedidoList) ) {
					int indexEncontrado = -1;
					int index = 0;
					for (DetallePedido item : detallePedidoList) {
						if(detalle.getCodpro() == item.getCodpro()) {
							indexEncontrado = index;
							break;
						}
						index++;
					}
					if(indexEncontrado >= 0) {
						detallePedidoList.remove(indexEncontrado);
					}
		}
	}
	public Long adicionar(Pedido pedido,List<DetallePedido> detalle) {
		try {
			String sql ="select coalesce(max(codped),0)+1 from pedido";
			Long cod = db.queryForObject(sql, Long.class);
			sql = "insert into pedido(codped,fecha,estado,codusu,celular,direccion,observacion,nit,razon_nit,codsuc) values(?,now(),?,?,?,?,?,?,?,?);";
			int res =db.update(sql,cod,Constantes.ESTADO.PENDIENTE,pedido.getCodusu(),pedido.getCelular(),pedido.getDireccion(),pedido.getObservacion(),pedido.getNit(),pedido.getRazon_nit(),pedido.getCodsuc());
			if(res>0) {
				adicionarDetalle(detalle, cod);
				return cod;
			}
			return -1L;
		} catch (Exception e) {
			System.out.println("erroral adicionar detalle de pedido"+e.toString());
			return -1L;
		}
	}
	public void adicionarDetalle(List<DetallePedido> detalle,Long codped) {
		if(isValidDetallePedidoList(detalle) ) {
			String sql ="insert into detallepedido(codped,codpro,cantidad,precio,subtotal) values(?,?,?,?,?);";
			for (DetallePedido det : detalle) {
				db.update(sql,codped,det.getCodpro(),det.getCantidad(),det.getPrecio(),det.getSubtotal());
			}
		}
	}
	public void eliminarDetalleSesion(HttpServletRequest request,DetallePedido detalle) {
			HttpSession sesion = request.getSession();
			@SuppressWarnings("unchecked")
			List<DetallePedido> detallePedidoList =  (List<DetallePedido>) sesion.getAttribute(Constantes.Sesiones.DETALLE);
			//Eliminar detalle de producto si ya existe
			eliminarDetalleExistente(detallePedidoList, detalle);
			sesion.setAttribute(Constantes.Sesiones.DETALLE, detallePedidoList);
	}
	public Float obtenerTotalPedidoSesion(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		List<DetallePedido> detallePedidoList =  (List<DetallePedido>) request.getSession().getAttribute(Constantes.Sesiones.DETALLE);
		
		return obtenerTotal(detallePedidoList);
	}
	public Float obtenerTotal(List<DetallePedido> detallePedidoList) {
		Float total =0f;
		if(isValidDetallePedidoList(detallePedidoList)) {
			for (DetallePedido detallePedido : detallePedidoList) {
				total +=detallePedido.getSubtotal();
			}
		}
		return total;
	}
	public Pedido obtenerPedido(Long codped) {
		try {
			String sql ="select p.*,concat(u.nombre,' ',u.ap) as xusuario,to_char(p.fecha,'DD/MM/YY') as xfecha,to_char(p.fentrega, 'DD/MM/YYYY HH:ii:ss') from pedido p\n" + 
					"join usuario u on u.codusu = p.codusu\n" + 
					"where codped =?";
			return db.queryForObject(sql,new BeanPropertyRowMapper<Pedido>(Pedido.class),codped);
		} catch (Exception e) {
			return null;
		}
	}
	public List<DetallePedido> obtenerDetallePedido(Long codped){
		try {
			String sql = "select dp.*,p.nombre as xproducto from detallepedido dp \n" + 
					"join producto p on p.codpro = dp.codpro\n" + 
					"where codped =?";
			return db.query(sql, new BeanPropertyRowMapper<DetallePedido>(DetallePedido.class),codped);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error al obtener detalles->"+e.toString());
			return null;
		}
	}
	public void cambiarEstado(Pedido pedido) {
		db.update("update pedido set estado =? where codped =?",pedido.getEstado(),pedido.getCodped());
	}
	public void asignarVenta(Pedido pedido) {
		db.update("update pedido set codven=? where codped =?",pedido.getCodven(),pedido.getCodped());
	}
	public void adicionarObservacion(Pedido pedido) {
		db.update("update pedido set observacion=? where codped =?",pedido.getObservacion(),pedido.getCodped());
	}
}
