package Servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Modelos.Mueble;

@Service
@Transactional
public class MuebleImpl implements MuebleS{
	private String consulta;
	private Logger LOGGER = Logger.getLogger("MuebleImpl");
	@Autowired
	private PosicionS posicionS;
	
	JdbcTemplate db;
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);
	}

	public List<Map<String, Object>> listar(int start, Integer estado, String search, int length) throws SQLException {
		if (estado==1) {
			int tot = db.queryForObject("select count(*) from mueble where estado=?", Integer.class, estado);
			if (search == null)
				search = "";
			consulta = "select mueble.*,row_number() OVER(ORDER BY mueble.codmueble) as RN,? as Tot,tipo_mueble.nombre as xtipo_mueble,(select string_agg(posicion.nombre,', ') from posicion where posicion.codmueble=mueble.codmueble) as posiciones from mueble join tipo_mueble on tipo_mueble.codtimu=mueble.codtimu where mueble.estado=? and upper(mueble.nombre) like concat('%',upper(?),'%') LIMIT ? OFFSET ?";
			return db.queryForList(consulta, tot, estado, search, length, start);
		} else
			return null;
	}
	public List<Map<String, Object>> listar() {
		try {
			List<Map<String,Object>> lista= db.queryForList("select mueble.*,tipo_mueble.nombre xtipomueble from mueble join tipo_mueble on tipo_mueble.codtimu=mueble.codtimu where mueble.estado=1;");
			for (Map<String, Object> map : lista) {
				map.put("posiciones", posicionS.listarPosicionesPorMueble(Integer.parseInt(map.get("codmueble").toString())));
			}
			return lista;
		} catch (Exception e) {
			LOGGER.info("listar:"+e.toString());
			return null;
		}
	}
	public Map<String, Object> obtener(Mueble obj) throws SQLException {
		try {
			consulta = "select * from mueble where codmueble=?";
			return db.queryForMap(consulta, obj.getCodmueble());
		} catch (Exception e) {
			LOGGER.info("obtener:" + e.toString());
			return null;
		}
	}
	public List<Map<String, Object>> obtenerPorTipoMueble(Integer codtimu)throws SQLException{
		try {
			return db.queryForList("select * from mueble where codtimu=?",codtimu);
		} catch (Exception e) {
			LOGGER.info("obtenerPorTipoMueble:"+e.toString());
			return null;
		}
	}
	public Integer adicionar(Mueble obj) {
		try {
			Integer codMueble=db.queryForObject("select coalesce(max(codmueble),0)+1 from mueble", Integer.class);
			consulta = "insert into mueble(codmueble,nombre,codtimu,estado) values(?,?,?,1);";
			db.update(consulta,codMueble, obj.getNombre(),obj.getCodtimu());
			return codMueble;
		} catch (Exception e) {
			LOGGER.info("adicionar:" + e.toString());
			return -1;
		}
	}
	public void modificar(Mueble obj) {
		try {
			consulta = "update mueble set nombre=?,codtimu=? where codmueble=?;";
			db.update(consulta, obj.getNombre(),obj.getCodtimu(), obj.getCodmueble());
		} catch (Exception e) {
			LOGGER.info("modificar:" + e.toString());
		}
	}
	public boolean eliminar(Mueble obj) throws SQLException{
		try {
			db.update("update mueble set estado=0 where codmueble=?", obj.getCodmueble());
			return true;
		} catch (Exception e) {
			LOGGER.info("eliminar:" + e.toString());
			return false;
		}
	}
}
