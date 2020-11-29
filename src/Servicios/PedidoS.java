package Servicios;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import Modelos.DetallePedido;
import Modelos.Pedido;
import Modelos.Usuario;

public interface PedidoS {
	public void adicionarPedidoSesion(HttpSession sesion,Usuario user);
	public void adicionarDetalleSesion(HttpServletRequest request,DetallePedido detalle);
	public Long adicionar(Pedido pedido,List<DetallePedido> detalle);
	public void eliminarDetalleSesion(HttpServletRequest request,DetallePedido detalle);
	public Float obtenerTotalPedidoSesion(HttpServletRequest request);
	public Pedido obtenerPedido(Long codped);
	public List<DetallePedido> obtenerDetallePedido(Long codped);
	public Float obtenerTotal(List<DetallePedido> detallePedidoList);
	public void cambiarEstado(Pedido pedido) ;
	public void asignarVenta(Pedido pedido);
	public void adicionarObservacion(Pedido pedido);
}
