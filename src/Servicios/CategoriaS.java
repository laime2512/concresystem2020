package Servicios;

import java.util.List;
import java.util.Map;

import Modelos.Categoria;

public interface CategoriaS {
	public List<Categoria> listar_todos();
	public List<Map<String, Object>> listar(int start,int estado,String search,int length);
	public Categoria obtener(int cod);
	public int generarCodigo();
	public boolean adicionar(Categoria m);
	public boolean modificar(Categoria obj);
	public boolean darEstado(int cod,int estado);
}
