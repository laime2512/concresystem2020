--https://www.cnblogs.com/Olimpic2008/articles/5105123.html
--Vista para listas los datos de almacen y sus relaciones
create view view_almacen
as
SELECT lugar.codlugar,
    producto.codpro,
    producto.codigobarra,
    producto.nombre,
    producto.concentracion,
    medida.nombre AS xmedida,
    tipo.nomtip AS xtipo,
    categoria.nomcat AS xcategoria,
    area.nombre AS xarea,
    laboratorio.nombre AS xlaboratorio,
    presentacion.nombre AS xpresentacion,
    posicion.nombre AS xposicion,
    mueble.nombre AS xmueble,
    almacen.unixcaja AS xunixcaja,
    sum(almacen.cantidad) AS sum_can_unid,
    sum(almacen.canxcaja) AS sum_can_caja,
    row_number() OVER (ORDER BY producto.codpro DESC) AS rn
   FROM ((((((((((almacen
     JOIN lugar ON ((almacen.codlugar = lugar.codlugar)))
     JOIN producto ON ((producto.codpro = lugar.codpro)))
     JOIN medida ON ((medida.codmed = producto.codmed)))
     JOIN tipo ON ((producto.codtip = tipo.codtip)))
     JOIN categoria ON ((categoria.codcat = tipo.codcat)))
     JOIN area ON ((area.codare = producto.codare)))
     JOIN laboratorio ON ((laboratorio.codlab = producto.codlab)))
     JOIN presentacion ON ((presentacion.codpre = producto.codpre)))
     JOIN posicion ON ((posicion.codposicion = lugar.codposicion)))
     JOIN mueble ON ((mueble.codmueble = posicion.codmueble)))
  GROUP BY lugar.codlugar, producto.codpro, medida.nombre, tipo.nomtip, categoria.nomcat, area.nombre, laboratorio.nombre, presentacion.nombre, posicion.nombre, mueble.nombre, almacen.unixcaja
  
  
  
  --Consulta para eliminar usuarios fisicacmente con estado inactivo
delete from proveedor where codproveedor in (select codusu from usuario where estado=0 and codusu not in(select codpro from compra));
delete from rolusu where login in (select login from accesousuario where codusu in (select codusu from usuario where estado=0))
delete from accesousuario where codusu in (select codusu from usuario where estado=0)
delete from acceso_sucursal where codusu in (select codusu from usuario where estado=0)
delete from cliente where codcli in (select codusu from usuario where estado=0 and codusu not in(select codcli from venta));
delete from usuario where estado = 0 and codusu!=10 and codusu!=0 and codusu not in (11,9);
-- Consulta para actualizar el precio de venta a precio de compra cuando su valor es INFINITY
update producto set pv_unit=pc_unit where pv_unit = 'Infinity'
--lista de productos incompletos
select * from producto
where concentracion is null or pc_unit is null or pc_caja is null or pc_paquete is null or pv_unit is null or pv_caja is null or 
pv_paquete is null or porcentaje_unidad is null or porcentaje_caja is null or porcentaje_paquete is null or porcentaje_descuento_caja is null or porcentaje_paquete is null or codpre is null or unixcaja is null or unixpaquete is null or uni_en_paquete is null or inventario_minimo_unidad is null or inventario_minimo_caja is null or inventario_minimo_paquete is null or porcentaje_unidad is null or porcentaje_caja is null or porcentaje_paquete is null or presentacion_unidad is null or presentacion_caja is null or presentacion_paquete is null or margen is null or tipo_compra is null and estado = 1