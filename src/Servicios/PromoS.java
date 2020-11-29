package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Modelos.DetallePromo;
import Modelos.Promocion;
import Wrap.PromoVec;

public interface PromoS {
	public Promocion obtener(Long codpromo) throws SQLException;
	public List<Promocion> obtenerPromocionesActuales();
	public List<DetallePromo> obtenerDetalles(Long codpromo);
	public List<Map<String, Object>> listar(int start, boolean estado, String search, int length);
	public Long adicionar(Promocion obj,PromoVec detalle) ;
	public Long generarCodigo();
	public boolean eliminar(Long codcompra);
}
