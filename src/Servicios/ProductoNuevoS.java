package Servicios;

import java.util.List;
import java.util.Map;

import Modelos.ProductoNuevo;

public interface ProductoNuevoS {
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length);
	public ProductoNuevo obtener(Long cod);
	public Long generarCodigo();
	public boolean adicionar(ProductoNuevo obj);
	public boolean modificar(ProductoNuevo obj);
	public boolean darEstado(Long cod,boolean estado);
}
