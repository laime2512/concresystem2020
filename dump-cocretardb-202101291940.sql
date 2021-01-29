--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.1

-- Started on 2021-01-29 19:40:18

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 250 (class 1255 OID 18644)
-- Name: calcular_cantidad(integer, character, integer, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.calcular_cantidad(p_cantidad integer, p_tipo character, p_unixcaja integer, p_unixpaquete double precision) RETURNS double precision
    LANGUAGE plpgsql
    AS $$BEGIN
	if(p_tipo = '2')then
		return cast(p_cantidad as float) / cast(p_unixcaja as float);
	end if;
	if(p_tipo = '3')then
		return cast(p_cantidad as float) / cast(p_unixcaja as float) / p_unixpaquete;
	end if;
	if(p_tipo = '1')then
		return p_cantidad;
	end if;
	RETURN -1;
END
$$;


ALTER FUNCTION public.calcular_cantidad(p_cantidad integer, p_tipo character, p_unixcaja integer, p_unixpaquete double precision) OWNER TO postgres;

--
-- TOC entry 251 (class 1255 OID 18645)
-- Name: tipo_cantidad(character); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.tipo_cantidad(p_tipo character) RETURNS character varying
    LANGUAGE plpgsql
    AS $$BEGIN
	if(p_tipo = '1') then
		RETURN 'Unidades';
	end if;
		if(p_tipo = '2') then
		RETURN 'Cajas';
		end if;
		if(p_tipo = '3') then
		RETURN 'Paquetes';
		end if;
		RETURN 'not';
END
$$;


ALTER FUNCTION public.tipo_cantidad(p_tipo character) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 200 (class 1259 OID 18646)
-- Name: acceso_sucursal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.acceso_sucursal (
    codusu bigint NOT NULL,
    codsuc integer NOT NULL,
    fecalta timestamp without time zone NOT NULL,
    fecbaja timestamp without time zone
);


ALTER TABLE public.acceso_sucursal OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 18649)
-- Name: accesousuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.accesousuario (
    login character varying(150) NOT NULL,
    passwd character varying(200) NOT NULL,
    estado smallint DEFAULT 1 NOT NULL,
    codusu bigint NOT NULL
);


ALTER TABLE public.accesousuario OWNER TO postgres;

--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN accesousuario.login; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.accesousuario.login IS 'nombre del usuario o login del usuario';


--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN accesousuario.passwd; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.accesousuario.passwd IS 'clave de acceso al usuario';


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN accesousuario.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.accesousuario.estado IS '1=activo, 0=anulado';


--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 201
-- Name: COLUMN accesousuario.codusu; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.accesousuario.codusu IS 'referencia tabla usuarios';


--
-- TOC entry 202 (class 1259 OID 18653)
-- Name: almacen; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.almacen (
    codlugar bigint NOT NULL,
    cantidad integer NOT NULL,
    codalmacen bigint NOT NULL
);


ALTER TABLE public.almacen OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 18656)
-- Name: area; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.area (
    codare integer NOT NULL,
    nombre character varying(100) NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE public.area OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 18659)
-- Name: backup; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.backup (
    cod bigint NOT NULL,
    xuser character varying(250) NOT NULL,
    descripcion character varying(500) NOT NULL
);


ALTER TABLE public.backup OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 18665)
-- Name: caja; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.caja (
    codcaja bigint NOT NULL,
    codusu bigint NOT NULL,
    fini timestamp(6) without time zone NOT NULL,
    ffin timestamp(6) without time zone,
    monini real NOT NULL,
    monfin real,
    monsistema real,
    estado smallint DEFAULT 1 NOT NULL,
    codsuc integer,
    observacion character varying(1500),
    tipo smallint
);


ALTER TABLE public.caja OWNER TO postgres;

--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN caja.codcaja; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.caja.codcaja IS 'codigo de caja';


--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN caja.codusu; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.caja.codusu IS 'referencia tabal usuarios';


--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN caja.fini; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.caja.fini IS 'fecha de inicio de la caja';


--
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN caja.ffin; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.caja.ffin IS 'fecha fin de la caja';


--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN caja.monini; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.caja.monini IS 'monto inicial de la caja';


--
-- TOC entry 3417 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN caja.monfin; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.caja.monfin IS 'monto final de la caja';


--
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN caja.monsistema; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.caja.monsistema IS 'monto del sistema ';


--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN caja.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.caja.estado IS '1=activo, 0=anulado';


--
-- TOC entry 3420 (class 0 OID 0)
-- Dependencies: 205
-- Name: COLUMN caja.tipo; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.caja.tipo IS '1=activo, 2=finalizado';


--
-- TOC entry 206 (class 1259 OID 18672)
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria (
    codcat smallint NOT NULL,
    nomcat character varying(500) NOT NULL,
    estado smallint NOT NULL
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- TOC entry 3421 (class 0 OID 0)
-- Dependencies: 206
-- Name: COLUMN categoria.codcat; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.categoria.codcat IS 'codigo de categoria';


--
-- TOC entry 3422 (class 0 OID 0)
-- Dependencies: 206
-- Name: COLUMN categoria.nomcat; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.categoria.nomcat IS 'nombre de la categoria';


--
-- TOC entry 3423 (class 0 OID 0)
-- Dependencies: 206
-- Name: COLUMN categoria.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.categoria.estado IS '1=activo, 0=anulado';


--
-- TOC entry 207 (class 1259 OID 18678)
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente (
    estado smallint DEFAULT 1 NOT NULL,
    nit character varying(15),
    direccion character varying(255),
    celular character varying(15),
    codcli bigint NOT NULL,
    razon_nit character varying(250)
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN cliente.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.cliente.estado IS '1=activo, 0=anulado';


--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN cliente.nit; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.cliente.nit IS 'nit del cliente';


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN cliente.direccion; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.cliente.direccion IS 'direccion del cliente';


--
-- TOC entry 3427 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN cliente.celular; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.cliente.celular IS 'celular del cliente';


--
-- TOC entry 3428 (class 0 OID 0)
-- Dependencies: 207
-- Name: COLUMN cliente.codcli; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.cliente.codcli IS 'codigo del cliente';


--
-- TOC entry 208 (class 1259 OID 18685)
-- Name: compra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.compra (
    codcom bigint NOT NULL,
    fecha timestamp(0) without time zone NOT NULL,
    estado smallint DEFAULT 1,
    codusu bigint NOT NULL,
    codpro bigint NOT NULL,
    tiponota smallint DEFAULT 1 NOT NULL,
    total real NOT NULL,
    descuento real,
    codcaja bigint,
    coddetcaja bigint,
    num bigint NOT NULL,
    formapago integer,
    bonificacion real,
    subtotal real,
    numnota character varying(50),
    codsuc integer,
    credito boolean,
    estado_credito boolean
);


ALTER TABLE public.compra OWNER TO postgres;

--
-- TOC entry 3429 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.codcom; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.codcom IS 'codigo de compra ';


--
-- TOC entry 3430 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.fecha; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.fecha IS 'fecha de la compra';


--
-- TOC entry 3431 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.estado IS '1=activo, 0=anulado';


--
-- TOC entry 3432 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.codusu; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.codusu IS 'referencia tabla usuarios';


--
-- TOC entry 3433 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.codpro; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.codpro IS 'referencia tabla producto';


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.tiponota; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.tiponota IS 'tipo de compra 1 = nota de venta y 2 =factura';


--
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.total; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.total IS 'total de la compra';


--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.descuento; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.descuento IS 'descuento especiald de la compra';


--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.formapago; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.formapago IS 'con dinero, con tarjeta';


--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.credito; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.credito IS 'true = si, false = no';


--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 208
-- Name: COLUMN compra.estado_credito; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.compra.estado_credito IS 'true = pendiente, false = finalizado';


--
-- TOC entry 209 (class 1259 OID 18690)
-- Name: cuenta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cuenta (
    codcuenta integer NOT NULL,
    nombre character varying(150) NOT NULL,
    estado boolean NOT NULL,
    tipo boolean NOT NULL
);


ALTER TABLE public.cuenta OWNER TO postgres;

--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 209
-- Name: COLUMN cuenta.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.cuenta.estado IS 'true= debe, false=haber';


--
-- TOC entry 210 (class 1259 OID 18693)
-- Name: detalle_margen; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detalle_margen (
    cod_margen integer NOT NULL,
    number_margin smallint NOT NULL,
    concept character varying(500) NOT NULL,
    type_margin character varying(50) NOT NULL,
    porcentaje_unidad real NOT NULL
);


ALTER TABLE public.detalle_margen OWNER TO postgres;

--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 210
-- Name: TABLE detalle_margen; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.detalle_margen IS 'Tabla de margen';


--
-- TOC entry 211 (class 1259 OID 18699)
-- Name: detalle_salida; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detalle_salida (
    codsal bigint NOT NULL,
    codalmacen bigint NOT NULL,
    cantidad integer NOT NULL,
    fingreso date,
    fvencimiento date,
    in_out boolean DEFAULT false,
    is_response boolean
);


ALTER TABLE public.detalle_salida OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 18703)
-- Name: detallecaja; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detallecaja (
    codcaja bigint NOT NULL,
    coddetcaja bigint NOT NULL,
    monto real NOT NULL,
    fecha timestamp(6) without time zone NOT NULL,
    estado smallint DEFAULT 1 NOT NULL,
    codcuenta integer
);


ALTER TABLE public.detallecaja OWNER TO postgres;

--
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN detallecaja.codcaja; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecaja.codcaja IS 'referencia tabla caja';


--
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN detallecaja.coddetcaja; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecaja.coddetcaja IS 'codigo de detalle de caja';


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN detallecaja.monto; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecaja.monto IS 'monto de la caja';


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN detallecaja.fecha; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecaja.fecha IS 'fecha de la caja';


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 212
-- Name: COLUMN detallecaja.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecaja.estado IS '1=debe, 0=haber';


--
-- TOC entry 213 (class 1259 OID 18707)
-- Name: detallecompra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detallecompra (
    codcom bigint NOT NULL,
    codpro bigint,
    cantidad smallint NOT NULL,
    precio real NOT NULL,
    subtotal real NOT NULL,
    fingreso date,
    fvencimiento date,
    codalmacen bigint,
    coddcom integer NOT NULL,
    impuestos real,
    devolucion boolean,
    descuento real,
    porcentaje_unidad real,
    porcentaje_caja real,
    porcentaje_paquete real,
    tipo_compra character(1)
);


ALTER TABLE public.detallecompra OWNER TO postgres;

--
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.codcom; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.codcom IS 'referencia a la tabla compra';


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.codpro; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.codpro IS 'referencia a la tabla producto';


--
-- TOC entry 3449 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.cantidad; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.cantidad IS 'cantidad de producto';


--
-- TOC entry 3450 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.precio; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.precio IS 'precio de producto';


--
-- TOC entry 3451 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.subtotal; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.subtotal IS 'subtotal';


--
-- TOC entry 3452 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.fingreso; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.fingreso IS 'fecha de ingreso';


--
-- TOC entry 3453 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.fvencimiento; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.fvencimiento IS 'fecha de vencimiento';


--
-- TOC entry 3454 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.codalmacen; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.codalmacen IS 'codigo almacen';


--
-- TOC entry 3455 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.coddcom; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.coddcom IS 'codigo detalle de compra';


--
-- TOC entry 3456 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN detallecompra.tipo_compra; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detallecompra.tipo_compra IS '1=compra por unidad, 2=compra por caja, 3=compra por paquete';


--
-- TOC entry 214 (class 1259 OID 18710)
-- Name: detallepedido; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detallepedido (
    codped bigint NOT NULL,
    codpro bigint NOT NULL,
    cantidad integer NOT NULL,
    precio real NOT NULL,
    subtotal real NOT NULL
);


ALTER TABLE public.detallepedido OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 18713)
-- Name: detallepromo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detallepromo (
    codpromo bigint NOT NULL,
    codpro bigint NOT NULL,
    descuento_unidad real NOT NULL,
    descuento_caja real,
    descuento_paquete real
);


ALTER TABLE public.detallepromo OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 18716)
-- Name: detalleventa; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detalleventa (
    codpro bigint NOT NULL,
    cantidad integer NOT NULL,
    codven bigint NOT NULL,
    precio real NOT NULL,
    subtotal real,
    codalmacen bigint NOT NULL,
    tipo_venta character(1),
    codpromo bigint
);


ALTER TABLE public.detalleventa OWNER TO postgres;

--
-- TOC entry 3457 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN detalleventa.codpro; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detalleventa.codpro IS 'referencia a la tabla producto';


--
-- TOC entry 3458 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN detalleventa.cantidad; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detalleventa.cantidad IS 'cantidad del producto';


--
-- TOC entry 3459 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN detalleventa.codven; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detalleventa.codven IS 'referencia a la tabla venta';


--
-- TOC entry 3460 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN detalleventa.precio; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detalleventa.precio IS 'precio del producto';


--
-- TOC entry 3461 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN detalleventa.subtotal; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.detalleventa.subtotal IS 'subtotal ';


--
-- TOC entry 217 (class 1259 OID 18719)
-- Name: dosificacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dosificacion (
    coddosificacion integer NOT NULL,
    razonsocial character varying(500) NOT NULL,
    direccion character varying(1000) NOT NULL,
    telefono character varying(200) NOT NULL,
    lugar character varying(400) NOT NULL,
    nit character varying(100) NOT NULL,
    numaut character varying(50) NOT NULL,
    actividad character varying(500) NOT NULL,
    llave character varying(500) NOT NULL,
    leyenda character varying(500) NOT NULL,
    mensaje character varying(500) NOT NULL,
    flimite date NOT NULL,
    ftramite date,
    numtramite character varying(50),
    sfc character varying(1000),
    est integer NOT NULL,
    codsuc integer,
    sigla character varying(25),
    numinifac integer,
    numfinfac integer
);


ALTER TABLE public.dosificacion OWNER TO postgres;

--
-- TOC entry 3462 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN dosificacion.est; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.dosificacion.est IS '0:eliminado, 1:activo,2=finalizado';


--
-- TOC entry 218 (class 1259 OID 18725)
-- Name: factura; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.factura (
    coddosificacion integer NOT NULL,
    numfac integer NOT NULL,
    fecfac timestamp(6) without time zone NOT NULL,
    nitfac character varying(100) NOT NULL,
    codcontrol character varying(100) NOT NULL,
    estado character varying(30) NOT NULL,
    cliente_nit character varying(255),
    codven bigint,
    codcom bigint,
    total real
);


ALTER TABLE public.factura OWNER TO postgres;

--
-- TOC entry 3463 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN factura.coddosificacion; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.factura.coddosificacion IS 'codigo de dosificacion';


--
-- TOC entry 3464 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN factura.numfac; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.factura.numfac IS 'numero de la factura';


--
-- TOC entry 3465 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN factura.fecfac; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.factura.fecfac IS 'fecha de la factura';


--
-- TOC entry 3466 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN factura.nitfac; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.factura.nitfac IS 'nit de la factura';


--
-- TOC entry 3467 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN factura.codcontrol; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.factura.codcontrol IS 'codigo de control';


--
-- TOC entry 3468 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN factura.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.factura.estado IS '1=activo, 0=anulado';


--
-- TOC entry 219 (class 1259 OID 18728)
-- Name: laboratorio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.laboratorio (
    codlab integer NOT NULL,
    nombre character varying(250) NOT NULL,
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE public.laboratorio OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 18732)
-- Name: lugar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lugar (
    codsuc integer NOT NULL,
    codpro bigint NOT NULL,
    codposicion integer NOT NULL,
    codlugar bigint NOT NULL
);


ALTER TABLE public.lugar OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 18735)
-- Name: margen; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.margen (
    cod_margen integer NOT NULL,
    codusu bigint NOT NULL,
    date_register timestamp without time zone NOT NULL,
    observacion character varying(500) NOT NULL,
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE public.margen OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 18742)
-- Name: medida; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.medida (
    codmed integer NOT NULL,
    nombre character varying(50) NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE public.medida OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 18745)
-- Name: menu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.menu (
    codm integer NOT NULL,
    nombre character varying(40) NOT NULL,
    estado smallint DEFAULT 1 NOT NULL
);


ALTER TABLE public.menu OWNER TO postgres;

--
-- TOC entry 3469 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN menu.codm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.menu.codm IS 'codigo del menu';


--
-- TOC entry 3470 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN menu.nombre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.menu.nombre IS 'nombre del menu';


--
-- TOC entry 3471 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN menu.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.menu.estado IS '1=activo, 0=anulado';


--
-- TOC entry 224 (class 1259 OID 18749)
-- Name: mepro; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mepro (
    codm integer NOT NULL,
    codp integer NOT NULL
);


ALTER TABLE public.mepro OWNER TO postgres;

--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mepro.codm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.mepro.codm IS 'referencia tabla menu';


--
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mepro.codp; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.mepro.codp IS 'referencia tabla procesos';


--
-- TOC entry 225 (class 1259 OID 18752)
-- Name: mueble; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mueble (
    codmueble integer NOT NULL,
    nombre character varying(255) NOT NULL,
    estado smallint DEFAULT 1,
    codtimu integer
);


ALTER TABLE public.mueble OWNER TO postgres;

--
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mueble.codmueble; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.mueble.codmueble IS 'codigo de mueble';


--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mueble.nombre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.mueble.nombre IS 'nombre de mueble';


--
-- TOC entry 226 (class 1259 OID 18756)
-- Name: pagocredito; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pagocredito (
    codcom bigint NOT NULL,
    fecha date NOT NULL,
    fecreg timestamp(6) without time zone NOT NULL,
    monto real NOT NULL,
    codusu bigint NOT NULL,
    observacion character varying(500)
);


ALTER TABLE public.pagocredito OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 18762)
-- Name: pedido; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pedido (
    codped bigint NOT NULL,
    fecha timestamp(6) without time zone NOT NULL,
    estado smallint DEFAULT 1 NOT NULL,
    codusu bigint NOT NULL,
    celular character varying(15) NOT NULL,
    direccion character varying(255),
    observacion character varying(500),
    fentrega timestamp(6) without time zone,
    coddelivery integer,
    nit character varying(25),
    razon_nit character varying(250),
    codven bigint,
    codsuc integer
);


ALTER TABLE public.pedido OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 18769)
-- Name: posicion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.posicion (
    codposicion integer NOT NULL,
    nombre character varying(255) NOT NULL,
    estado smallint DEFAULT 1 NOT NULL,
    codmueble integer
);


ALTER TABLE public.posicion OWNER TO postgres;

--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN posicion.codposicion; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.posicion.codposicion IS 'codigo de posicion ';


--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN posicion.nombre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.posicion.nombre IS 'nombre de posicion';


--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN posicion.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.posicion.estado IS '1=activo, 0=anulado';


--
-- TOC entry 229 (class 1259 OID 18773)
-- Name: presentacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.presentacion (
    codpre integer NOT NULL,
    nombre character varying NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE public.presentacion OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 18779)
-- Name: proceso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proceso (
    codp integer NOT NULL,
    nombre character varying(40) NOT NULL,
    enlace character varying(60) NOT NULL,
    estado smallint DEFAULT 1 NOT NULL
);


ALTER TABLE public.proceso OWNER TO postgres;

--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN proceso.codp; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proceso.codp IS 'codigo de procesos';


--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN proceso.nombre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proceso.nombre IS 'nombre del proceso';


--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN proceso.enlace; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proceso.enlace IS 'link o enlace del proceso';


--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN proceso.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proceso.estado IS '1=activo, 0=anulado';


--
-- TOC entry 231 (class 1259 OID 18783)
-- Name: producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto (
    codpro bigint NOT NULL,
    nombre character varying(150),
    estado smallint DEFAULT 1,
    foto character varying(40),
    codtip smallint,
    generico character varying(255),
    codigobarra character varying(30),
    codlab integer,
    concentracion character varying(25),
    codmed integer,
    codare integer,
    pc_unit real,
    pv_unit real,
    porcentaje_unidad real,
    codpre integer,
    controlado boolean DEFAULT false,
    inventario_minimo_unidad real,
    pareto character varying(25),
    unixcaja integer DEFAULT 0,
    pv_caja real,
    pv_descuento_caja real,
    pc_caja real,
    unixpaquete real,
    uni_en_paquete real,
    inventario_minimo_caja real,
    inventario_minimo_paquete real,
    pc_paquete real,
    pv_paquete real,
    porcentaje_caja real,
    porcentaje_paquete real,
    tipo_compra character(1),
    pv_descuento_paquete real,
    presentacion_unidad character varying(25),
    presentacion_caja character varying(25),
    presentacion_paquete character varying(25),
    margen character(1),
    porcentaje_descuento_caja real,
    porcentaje_descuento_paquete real
);


ALTER TABLE public.producto OWNER TO postgres;

--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN producto.codpro; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.producto.codpro IS 'codigo de producto';


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN producto.nombre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.producto.nombre IS 'nombre de producto';


--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN producto.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.producto.estado IS '1=activo, 0=anulado';


--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN producto.foto; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.producto.foto IS 'foto del producto';


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN producto.codtip; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.producto.codtip IS 'codigo del tipo de producto';


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN producto.generico; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.producto.generico IS 'composicion del producto';


--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN producto.codigobarra; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.producto.codigobarra IS 'codigo de barra del producto';


--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN producto.codlab; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.producto.codlab IS 'codigo de laboratorio';


--
-- TOC entry 232 (class 1259 OID 18792)
-- Name: producto_nuevo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto_nuevo (
    cod_pro_nuevo bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion character varying(300),
    estado boolean DEFAULT true NOT NULL,
    codusu bigint NOT NULL
);


ALTER TABLE public.producto_nuevo OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 18796)
-- Name: promocion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.promocion (
    codpromo bigint NOT NULL,
    fcreacion date NOT NULL,
    fini date NOT NULL,
    ffin date NOT NULL,
    titulo character varying(100) NOT NULL,
    descripcion character varying(500),
    codusu bigint NOT NULL,
    estpromo boolean NOT NULL,
    gestion integer NOT NULL
);


ALTER TABLE public.promocion OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 18802)
-- Name: proveedor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proveedor (
    codproveedor bigint NOT NULL,
    nombre character varying(40) NOT NULL,
    nit integer NOT NULL,
    direccion character varying(40) NOT NULL,
    telefono character varying(20) NOT NULL,
    estado smallint DEFAULT 1
);


ALTER TABLE public.proveedor OWNER TO postgres;

--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN proveedor.codproveedor; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proveedor.codproveedor IS 'codigo de proveedor';


--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN proveedor.nombre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proveedor.nombre IS 'nombre de proveedor';


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN proveedor.nit; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proveedor.nit IS 'nit de proveedor';


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN proveedor.direccion; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proveedor.direccion IS 'direccion de proveedor';


--
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN proveedor.telefono; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proveedor.telefono IS 'telefono de proveedor';


--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN proveedor.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.proveedor.estado IS '1=activo, 0=anulado';


--
-- TOC entry 235 (class 1259 OID 18806)
-- Name: rol; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rol (
    codr integer NOT NULL,
    nombre character varying(40) NOT NULL,
    estado smallint DEFAULT 1 NOT NULL,
    acceso_venta boolean DEFAULT false
);


ALTER TABLE public.rol OWNER TO postgres;

--
-- TOC entry 3497 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN rol.codr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rol.codr IS 'codigo del rol';


--
-- TOC entry 3498 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN rol.nombre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rol.nombre IS 'nombre del rol';


--
-- TOC entry 3499 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN rol.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rol.estado IS '1=activo, 0=anulado';


--
-- TOC entry 3500 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN rol.acceso_venta; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rol.acceso_venta IS 'Ventas: true=acceso completo, false=acceso restringido';


--
-- TOC entry 236 (class 1259 OID 18811)
-- Name: rolme; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rolme (
    codr integer NOT NULL,
    codm integer NOT NULL
);


ALTER TABLE public.rolme OWNER TO postgres;

--
-- TOC entry 3501 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN rolme.codr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rolme.codr IS 'referencia tabla roles';


--
-- TOC entry 3502 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN rolme.codm; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rolme.codm IS 'referencia tabla menu';


--
-- TOC entry 237 (class 1259 OID 18814)
-- Name: rolusu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rolusu (
    codr integer NOT NULL,
    login character varying(150) NOT NULL
);


ALTER TABLE public.rolusu OWNER TO postgres;

--
-- TOC entry 3503 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN rolusu.codr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rolusu.codr IS 'referencia tabla roles';


--
-- TOC entry 3504 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN rolusu.login; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.rolusu.login IS 'refencia tabla accesousuario';


--
-- TOC entry 238 (class 1259 OID 18817)
-- Name: salida; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.salida (
    codsal bigint NOT NULL,
    fsalida date,
    codusu bigint,
    tipo smallint,
    descripcion character varying(500),
    estado character varying(1),
    est boolean,
    numero bigint NOT NULL,
    suc_origen integer,
    suc_destino integer,
    in_out boolean,
    solucion smallint,
    monto real,
    conclusion character varying(500)
);


ALTER TABLE public.salida OWNER TO postgres;

--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN salida.tipo; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.salida.tipo IS '1= Productos en mal estado,
2= Salida a otra farmacia egreso,
3=Medicamentos vencidos,
4= Perdida de medicamentos,
5= Uso de farmacia,
6= Disminucion de medicamento por usuario
7=Aumento de medicamento por usuario
8=Entrada de medicamento
9=salida
10=entrada de otra farmacia
';


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN salida.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.salida.estado IS 'p=pendiente
a=aceptado
r=rechazado';


--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN salida.in_out; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.salida.in_out IS 'true= ingreso, false= salida';


--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN salida.solucion; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.salida.solucion IS '1= ';


--
-- TOC entry 239 (class 1259 OID 18823)
-- Name: sucursal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sucursal (
    codsuc integer NOT NULL,
    nombre character varying(500) NOT NULL,
    telefono character varying(50) NOT NULL,
    estado boolean NOT NULL,
    direccion character varying(300)
);


ALTER TABLE public.sucursal OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 18829)
-- Name: tipo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo (
    codtip smallint NOT NULL,
    nomtip character varying(500) NOT NULL,
    estado smallint NOT NULL,
    codcat smallint NOT NULL
);


ALTER TABLE public.tipo OWNER TO postgres;

--
-- TOC entry 3509 (class 0 OID 0)
-- Dependencies: 240
-- Name: COLUMN tipo.codtip; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tipo.codtip IS 'codigo de tipo';


--
-- TOC entry 3510 (class 0 OID 0)
-- Dependencies: 240
-- Name: COLUMN tipo.nomtip; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tipo.nomtip IS 'nombre de tipo';


--
-- TOC entry 3511 (class 0 OID 0)
-- Dependencies: 240
-- Name: COLUMN tipo.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tipo.estado IS '1=activo, 0=anulado';


--
-- TOC entry 3512 (class 0 OID 0)
-- Dependencies: 240
-- Name: COLUMN tipo.codcat; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.tipo.codcat IS 'referencia a la tabla categoria';


--
-- TOC entry 241 (class 1259 OID 18835)
-- Name: tipo_mueble; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_mueble (
    codtimu integer NOT NULL,
    nombre character varying(100) NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE public.tipo_mueble OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 18838)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    codusu bigint NOT NULL,
    nombre character varying(60) DEFAULT 1 NOT NULL,
    ap character varying(40),
    am character varying(40),
    genero character varying(1) NOT NULL,
    estado smallint DEFAULT 1 NOT NULL,
    tipoper character varying(1) NOT NULL,
    foto character varying(40),
    ci character varying(15),
    fnac date,
    ecivil character varying(20),
    alias character varying(25)
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 3513 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN usuario.codusu; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.codusu IS 'codigo de la persona';


--
-- TOC entry 3514 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN usuario.nombre; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.nombre IS 'nombre de la persona';


--
-- TOC entry 3515 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN usuario.ap; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.ap IS 'apellido paterno';


--
-- TOC entry 3516 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN usuario.am; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.am IS 'apellido materno';


--
-- TOC entry 3517 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN usuario.genero; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.genero IS 'genero (f=femenino, m=masculino)';


--
-- TOC entry 3518 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN usuario.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.estado IS '1=activo, 0=anulado';


--
-- TOC entry 3519 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN usuario.tipoper; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.tipoper IS 'a=administrativo, p=publico';


--
-- TOC entry 3520 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN usuario.foto; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.foto IS 'foto de la persona';


--
-- TOC entry 3521 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN usuario.ci; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.usuario.ci IS 'cedula de la persona';


--
-- TOC entry 243 (class 1259 OID 18843)
-- Name: venta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venta (
    codven bigint NOT NULL,
    fecha date NOT NULL,
    estado smallint DEFAULT 1 NOT NULL,
    codcli bigint NOT NULL,
    tiponota smallint,
    codcaja bigint NOT NULL,
    coddetcaja bigint NOT NULL,
    formapago smallint,
    total real,
    codusu bigint,
    total_pagado real,
    cambio real,
    codsuc integer
);


ALTER TABLE public.venta OWNER TO postgres;

--
-- TOC entry 3522 (class 0 OID 0)
-- Dependencies: 243
-- Name: TABLE venta; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE public.venta IS '1=pendiente,2=aceptado,3=rechazado';


--
-- TOC entry 3523 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN venta.codven; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venta.codven IS 'codigo de venta';


--
-- TOC entry 3524 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN venta.fecha; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venta.fecha IS 'fecha de creacion';


--
-- TOC entry 3525 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN venta.estado; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venta.estado IS 'estado 1=activo, 0
';


--
-- TOC entry 3526 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN venta.codcli; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venta.codcli IS 'codigo de cliente';


--
-- TOC entry 3527 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN venta.tiponota; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venta.tiponota IS '1=pendiente,2=aceptado,3=rechazado';


--
-- TOC entry 3528 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN venta.codcaja; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venta.codcaja IS 'referencia tabla caja';


--
-- TOC entry 3529 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN venta.coddetcaja; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venta.coddetcaja IS 'refencia a la tabla detalle de caja';


--
-- TOC entry 3530 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN venta.formapago; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.venta.formapago IS '1 = al contado, 2 = al credito';


--
-- TOC entry 244 (class 1259 OID 18847)
-- Name: view_almacen; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_almacen AS
 SELECT p.codpro,
    p.nombre,
    p.foto,
    p.codtip,
    p.generico,
    p.codigobarra,
    p.codlab,
    p.concentracion,
    p.codmed,
    p.codare,
    p.pc_unit,
    p.pv_unit,
    p.porcentaje_unidad,
    p.estado,
    p.controlado,
    p.inventario_minimo_unidad,
    p.pareto,
    p.unixcaja,
    p.pc_caja,
    p.pv_caja,
    p.pv_descuento_caja,
    p.unixpaquete,
    p.uni_en_paquete,
    p.inventario_minimo_caja,
    p.inventario_minimo_paquete,
    p.pc_paquete,
    p.pv_paquete,
    p.porcentaje_caja,
    p.porcentaje_paquete,
    p.tipo_compra,
    p.pv_descuento_paquete,
    p.presentacion_unidad,
    p.presentacion_caja,
    p.presentacion_paquete,
    p.margen,
    p.porcentaje_descuento_caja,
    p.porcentaje_descuento_paquete,
    t.nomtip AS xtipo,
    c.nomcat AS xcategoria,
    l.nombre AS xlaboratorio,
    a.nombre AS xarea,
    m.nombre AS xmedida,
    lugar.codsuc,
    sum(almacen.cantidad) AS cantidad
   FROM (((((((public.almacen
     JOIN public.lugar ON ((lugar.codlugar = almacen.codlugar)))
     JOIN public.producto p ON ((p.codpro = lugar.codpro)))
     JOIN public.tipo t ON ((t.codtip = p.codtip)))
     JOIN public.categoria c ON ((c.codcat = t.codcat)))
     JOIN public.laboratorio l ON ((l.codlab = p.codlab)))
     JOIN public.medida m ON ((m.codmed = p.codmed)))
     JOIN public.area a ON ((a.codare = p.codare)))
  GROUP BY p.codpro, p.nombre, p.foto, p.codtip, p.generico, p.codigobarra, p.codlab, p.concentracion, p.codmed, p.codare, p.pc_unit, p.pv_unit, p.porcentaje_unidad, p.estado, p.controlado, p.inventario_minimo_unidad, p.pareto, p.unixcaja, p.pc_caja, p.pv_caja, p.pv_descuento_caja, p.unixpaquete, p.uni_en_paquete, p.inventario_minimo_caja, p.inventario_minimo_paquete, p.pc_paquete, p.pv_paquete, p.porcentaje_caja, p.porcentaje_paquete, p.tipo_compra, p.pv_descuento_paquete, p.presentacion_unidad, p.presentacion_caja, p.presentacion_paquete, p.margen, p.porcentaje_descuento_caja, p.porcentaje_descuento_paquete, t.nomtip, c.nomcat, l.nombre, a.nombre, m.nombre, lugar.codsuc
 HAVING (sum(almacen.cantidad) > 0);


ALTER TABLE public.view_almacen OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 18852)
-- Name: view_compra; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_compra AS
 SELECT compra.codcom,
    compra.fecha,
    compra.estado,
    compra.codusu,
    compra.codpro,
    compra.tiponota,
    compra.total,
    compra.descuento,
    compra.codcaja,
    compra.coddetcaja,
    compra.num,
    compra.formapago,
    compra.bonificacion,
    compra.subtotal,
    compra.numnota,
    compra.codsuc,
    compra.credito,
    compra.estado_credito,
    concat(usuario.nombre, ' ', usuario.ap) AS xusuario,
    proveedor.nombre AS xproveedor,
    to_char(compra.fecha, 'dd/MM/yyyy'::text) AS xfecha,
    ( SELECT COALESCE(sum(pagocredito.monto), (0)::real) AS "coalesce"
           FROM public.pagocredito
          WHERE (pagocredito.codcom = compra.codcom)) AS acuenta
   FROM ((public.compra
     JOIN public.proveedor ON ((proveedor.codproveedor = compra.codpro)))
     JOIN public.usuario ON ((usuario.codusu = compra.codusu)));


ALTER TABLE public.view_compra OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 18857)
-- Name: view_detalle_salida; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_detalle_salida AS
 SELECT ds.codsal,
    ds.codalmacen,
    ds.cantidad,
    ds.fingreso,
    ds.fvencimiento,
    ds.is_response,
    ds.in_out,
    l.codlugar,
    p.codpro,
    concat(p.nombre, ' (', p.generico, ')') AS nombre,
    to_char((ds.fingreso)::timestamp with time zone, 'dd/MM/yy'::text) AS xfingreso,
    to_char((ds.fvencimiento)::timestamp with time zone, 'dd/MM/yy'::text) AS xfvencimiento
   FROM (((public.detalle_salida ds
     JOIN public.almacen a ON ((a.codalmacen = ds.codalmacen)))
     JOIN public.lugar l ON ((l.codlugar = a.codlugar)))
     JOIN public.producto p ON ((p.codpro = l.codpro)))
  ORDER BY ds.is_response;


ALTER TABLE public.view_detalle_salida OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 18862)
-- Name: view_detalle_venta; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_detalle_venta AS
 SELECT detalleventa.codpro,
    detalleventa.cantidad,
    detalleventa.codven,
    detalleventa.precio,
    detalleventa.subtotal,
    detalleventa.codalmacen,
    detalleventa.tipo_venta,
    detalleventa.codpromo,
    concat(tipo.nomtip, ' ', producto.nombre, ' (', producto.generico, ') ', producto.concentracion, ' ', medida.nombre) AS xproducto,
    producto.unixcaja,
    producto.unixpaquete,
    public.calcular_cantidad(detalleventa.cantidad, detalleventa.tipo_venta, producto.unixcaja, (producto.unixpaquete)::double precision) AS cantidad_tipo_venta,
    public.tipo_cantidad(detalleventa.tipo_venta) AS xtipo_venta
   FROM (((public.detalleventa
     JOIN public.producto ON ((producto.codpro = detalleventa.codpro)))
     JOIN public.medida ON ((medida.codmed = producto.codmed)))
     JOIN public.tipo ON ((tipo.codtip = producto.codtip)));


ALTER TABLE public.view_detalle_venta OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 18867)
-- Name: view_producto; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_producto AS
 SELECT p.codpro,
    p.nombre,
    p.estado,
    p.foto,
    p.codtip,
    p.generico,
    p.codigobarra,
    p.codlab,
    p.concentracion,
    p.codmed,
    p.codare,
    p.pc_unit,
    p.pv_unit,
    p.porcentaje_unidad,
    p.codpre,
    p.controlado,
    p.inventario_minimo_unidad,
    p.pareto,
    p.unixcaja,
    p.pv_caja,
    p.pv_descuento_caja,
    p.pc_caja,
    p.unixpaquete,
    p.uni_en_paquete,
    p.inventario_minimo_caja,
    p.inventario_minimo_paquete,
    p.pc_paquete,
    p.pv_paquete,
    p.porcentaje_caja,
    p.porcentaje_paquete,
    p.tipo_compra,
    p.pv_descuento_paquete,
    p.presentacion_unidad,
    p.presentacion_caja,
    p.presentacion_paquete,
    p.margen,
    p.porcentaje_descuento_caja,
    p.porcentaje_descuento_paquete,
    t.nomtip AS xtipo,
    c.nomcat AS xcategoria,
    l.nombre AS xlaboratorio,
    a.nombre AS xarea,
    m.nombre AS xmedida
   FROM (((((public.producto p
     JOIN public.tipo t ON ((t.codtip = p.codtip)))
     JOIN public.categoria c ON ((c.codcat = t.codcat)))
     JOIN public.laboratorio l ON ((l.codlab = p.codlab)))
     JOIN public.medida m ON ((m.codmed = p.codmed)))
     JOIN public.area a ON ((a.codare = p.codare)))
  WHERE (p.estado = 1);


ALTER TABLE public.view_producto OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 18872)
-- Name: view_venta; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.view_venta AS
 SELECT venta.codven,
    venta.fecha,
    venta.estado,
    venta.codcli,
    venta.tiponota,
    venta.codcaja,
    venta.coddetcaja,
    venta.formapago,
    venta.total,
    venta.codusu,
    venta.total_pagado,
    venta.cambio,
    venta.codsuc,
    to_char((venta.fecha)::timestamp with time zone, 'DD/MM/YY'::text) AS xfecha,
    concat(usuario.nombre, ' ', usuario.ap) AS xcliente,
    concat(u.nombre, ' ', u.ap) AS xusuario,
    ( SELECT count(*) AS facturado
           FROM public.factura factura_1
          WHERE ((factura_1.codven = venta.codven) AND ((factura_1.estado)::text = 'activo'::text))) AS facturado,
    factura.cliente_nit,
    factura.nitfac,
    factura.estado AS estado_factura,
    factura.numfac
   FROM ((((public.venta
     JOIN public.cliente ON ((venta.codcli = cliente.codcli)))
     JOIN public.usuario ON ((usuario.codusu = cliente.codcli)))
     JOIN public.usuario u ON ((u.codusu = venta.codusu)))
     LEFT JOIN public.factura ON ((factura.codven = venta.codven)));


ALTER TABLE public.view_venta OWNER TO postgres;

--
-- TOC entry 3358 (class 0 OID 18646)
-- Dependencies: 200
-- Data for Name: acceso_sucursal; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.acceso_sucursal (codusu, codsuc, fecalta, fecbaja) FROM stdin;
1	1	2020-11-29 19:08:30	\N
7	1	2021-01-12 22:39:31.698676	\N
8	1	2021-01-12 23:06:28.918977	\N
\.


--
-- TOC entry 3359 (class 0 OID 18649)
-- Dependencies: 201
-- Data for Name: accesousuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.accesousuario (login, passwd, estado, codusu) FROM stdin;
admin	admin	1	1
		1	3
cli	cli	1	4
asdasd	asdasd	1	5
edserrudo	concretar2020	1	7
vendedor1	vendedor1	1	8
\.


--
-- TOC entry 3360 (class 0 OID 18653)
-- Dependencies: 202
-- Data for Name: almacen; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.almacen (codlugar, cantidad, codalmacen) FROM stdin;
3	3	3
2	33	4
4	15	5
1	3	1
2	13	2
1	25	6
\.


--
-- TOC entry 3361 (class 0 OID 18656)
-- Dependencies: 203
-- Data for Name: area; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.area (codare, nombre, estado) FROM stdin;
0	S/A	t
3	Concretar	t
4	Proyectar	t
\.


--
-- TOC entry 3362 (class 0 OID 18659)
-- Dependencies: 204
-- Data for Name: backup; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.backup (cod, xuser, descripcion) FROM stdin;
1	20191215_103629.backup	Carla Sofia Grageda
\.


--
-- TOC entry 3363 (class 0 OID 18665)
-- Dependencies: 205
-- Data for Name: caja; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.caja (codcaja, codusu, fini, ffin, monini, monfin, monsistema, estado, codsuc, observacion, tipo) FROM stdin;
1	1	2020-12-01 08:46:16.181398	\N	100	\N	100	1	1	\N	1
\.


--
-- TOC entry 3364 (class 0 OID 18672)
-- Dependencies: 206
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categoria (codcat, nomcat, estado) FROM stdin;
6	Productos Concretar	0
10	Instrumentos Proyectar	0
0	S/c	1
11	Concreto	1
12	Impermeabilizacion	1
13	Sellado Y Pegado	1
14	Cubiertas Y Techos	1
15	Pisos Industriales	1
16	Reparacion Y Rehab	1
17	Grouting Y Anclajes	1
18	Industria	1
19	Instrumentos Proyectar	1
\.


--
-- TOC entry 3365 (class 0 OID 18678)
-- Dependencies: 207
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cliente (estado, nit, direccion, celular, codcli, razon_nit) FROM stdin;
1	321654987	\N	\N	3	\N
0	23123213	\N	\N	5	\N
0	1123123123	\N	\N	4	\N
1	22222223333	\N	\N	6	\N
\.


--
-- TOC entry 3366 (class 0 OID 18685)
-- Dependencies: 208
-- Data for Name: compra; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.compra (codcom, fecha, estado, codusu, codpro, tiponota, total, descuento, codcaja, coddetcaja, num, formapago, bonificacion, subtotal, numnota, codsuc, credito, estado_credito) FROM stdin;
1	2020-11-30 00:00:00	1	1	2	1	400	0	\N	\N	1	\N	0	400	1088	1	f	f
2	2020-11-30 00:00:00	1	1	2	1	290	10	\N	\N	2	\N	0	300	3215	1	t	t
3	2020-11-30 00:00:00	1	1	2	1	732	0	\N	\N	3	\N	0	732	234324	1	f	f
4	2020-12-01 00:00:00	1	1	2	2	375	0	\N	\N	4	\N	0	375	12334567	1	f	f
\.


--
-- TOC entry 3367 (class 0 OID 18690)
-- Dependencies: 209
-- Data for Name: cuenta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cuenta (codcuenta, nombre, estado, tipo) FROM stdin;
101	Ingreso	t	t
102	Ventas	t	t
104	Traspaso de ingreso	t	t
103	Cuentas por cobrar	t	t
201	Egreso	f	t
202	Compras	f	t
203	Cuentas por pagar	f	t
204	Traspaso de egreso	f	t
205	Reversion de venta	f	t
105	Reversion de compra	t	t
\.


--
-- TOC entry 3368 (class 0 OID 18693)
-- Dependencies: 210
-- Data for Name: detalle_margen; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detalle_margen (cod_margen, number_margin, concept, type_margin, porcentaje_unidad) FROM stdin;
1	1	el primer margen	TIPO MARGEN	1
\.


--
-- TOC entry 3369 (class 0 OID 18699)
-- Dependencies: 211
-- Data for Name: detalle_salida; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detalle_salida (codsal, codalmacen, cantidad, fingreso, fvencimiento, in_out, is_response) FROM stdin;
1	6	25	2022-01-25	2021-01-25	t	t
\.


--
-- TOC entry 3370 (class 0 OID 18703)
-- Dependencies: 212
-- Data for Name: detallecaja; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detallecaja (codcaja, coddetcaja, monto, fecha, estado, codcuenta) FROM stdin;
1	1	108.6	2020-12-01 08:51:02.279004	1	102
\.


--
-- TOC entry 3371 (class 0 OID 18707)
-- Dependencies: 213
-- Data for Name: detallecompra; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detallecompra (codcom, codpro, cantidad, precio, subtotal, fingreso, fvencimiento, codalmacen, coddcom, impuestos, devolucion, descuento, porcentaje_unidad, porcentaje_caja, porcentaje_paquete, tipo_compra) FROM stdin;
1	1	4	100	400	2020-11-30	2021-12-25	1	1	0	f	0	2	2	2	1
2	4	15	20	300	2020-11-30	\N	2	1	0	f	0	10	10	10	1
3	3	3	211	633	2020-11-30	\N	3	1	0	f	0	5	5	5	1
3	4	33	3	99	2020-11-30	\N	4	2	0	f	0	10	10	10	1
4	6	15	25	375	2020-12-01	2021-12-25	5	1	0	f	0	10	10	10	1
\.


--
-- TOC entry 3372 (class 0 OID 18710)
-- Dependencies: 214
-- Data for Name: detallepedido; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detallepedido (codped, codpro, cantidad, precio, subtotal) FROM stdin;
\.


--
-- TOC entry 3373 (class 0 OID 18713)
-- Dependencies: 215
-- Data for Name: detallepromo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detallepromo (codpromo, codpro, descuento_unidad, descuento_caja, descuento_paquete) FROM stdin;
\.


--
-- TOC entry 3374 (class 0 OID 18716)
-- Dependencies: 216
-- Data for Name: detalleventa; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detalleventa (codpro, cantidad, codven, precio, subtotal, codalmacen, tipo_venta, codpromo) FROM stdin;
1	1	1	102	102	1	1	\N
4	2	1	3.3	6.6	2	1	\N
\.


--
-- TOC entry 3375 (class 0 OID 18719)
-- Dependencies: 217
-- Data for Name: dosificacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dosificacion (coddosificacion, razonsocial, direccion, telefono, lugar, nit, numaut, actividad, llave, leyenda, mensaje, flimite, ftramite, numtramite, sfc, est, codsuc, sigla, numinifac, numfinfac) FROM stdin;
1	concretar ltd	basasdsd	1321546	tarija- boliva	32165489	123	venta de productos	-L92%WxXbM3JU_I\\9C3f8X\\DvY]dFSTDFER#9WKkIsRK]tEpSZ]BEU3]EGE=T]V2	hola	hola	2020-12-25	2020-12-01	4255454	jhjgjhkjhkj	0	1	concretar	1	200
2	concretar ltd	basasdsd	1321546	tarija- boliva	5978995	283401600000395	venta de productos	rT{%LEz#{Eh#38e6k@UGB$WD$@@]SCWDVVcUhKa=V9hD4IxPCI=TJD[J-edC59)V	hola	hola	2016-03-01	2020-12-01	9613	jhjgjhkjhkj	1	\N	concretar	1	200
\.


--
-- TOC entry 3376 (class 0 OID 18725)
-- Dependencies: 218
-- Data for Name: factura; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.factura (coddosificacion, numfac, fecfac, nitfac, codcontrol, estado, cliente_nit, codven, codcom, total) FROM stdin;
\.


--
-- TOC entry 3377 (class 0 OID 18728)
-- Dependencies: 219
-- Data for Name: laboratorio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.laboratorio (codlab, nombre, estado) FROM stdin;
1	SIKA	t
10	HT 225 (Taiwan)	t
9	DCR (Italia)	t
8	Snd Way (Taiwan)	t
7	Floureon (Taiwan)	t
6	Bosch (Alemania)	t
5	Stanley (Americano)	t
4	Compas (Taiwan)	t
3	Motorola (Americano)	t
2	Generico (Korea del Sur)	t
11	marga generica	t
\.


--
-- TOC entry 3378 (class 0 OID 18732)
-- Dependencies: 220
-- Data for Name: lugar; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lugar (codsuc, codpro, codposicion, codlugar) FROM stdin;
1	1	0	1
1	4	0	2
1	3	0	3
1	6	0	4
\.


--
-- TOC entry 3379 (class 0 OID 18735)
-- Dependencies: 221
-- Data for Name: margen; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.margen (cod_margen, codusu, date_register, observacion, estado) FROM stdin;
1	1	2020-11-30 22:53:29.310985	Sin observaciones	t
\.


--
-- TOC entry 3380 (class 0 OID 18742)
-- Dependencies: 222
-- Data for Name: medida; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.medida (codmed, nombre, estado) FROM stdin;
2	l (Litro)	t
5	unid (Unidades)	t
8	m (Metros)	t
9	cc (Centimetros Cubicos)	t
11	kg (Kilogramo)	t
13	ug (Microgramo)	t
0	Sin medida	t
1	ml (Mililitro)	t
14	pzas (piezas)	t
15	miligramo (mg)	t
\.


--
-- TOC entry 3381 (class 0 OID 18745)
-- Dependencies: 223
-- Data for Name: menu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.menu (codm, nombre, estado) FROM stdin;
5	Transacciones	1
7	Gestion Categorias	0
13	Cambiar Clave	1
2	Registro Productos	1
9	Parametros del Producto	1
3	Sistema	1
4	Registro Ventas	1
6	Reportes	1
1	Registro Personas	1
8	Gestion Tipos	0
10	Menu Sistema	0
12	Clientes	0
11	Menu Vendedor	0
\.


--
-- TOC entry 3382 (class 0 OID 18749)
-- Dependencies: 224
-- Data for Name: mepro; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mepro (codm, codp) FROM stdin;
4	8
10	6
7	9
12	27
12	29
13	33
6	12
6	28
6	32
11	2
11	5
11	3
11	12
11	25
11	8
1	1
1	3
1	13
2	2
2	23
2	31
3	6
3	7
3	34
5	5
5	4
5	14
5	10
5	22
5	24
9	17
9	20
9	9
9	15
9	11
\.


--
-- TOC entry 3383 (class 0 OID 18752)
-- Dependencies: 225
-- Data for Name: mueble; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mueble (codmueble, nombre, estado, codtimu) FROM stdin;
7	H mod	0	1
0	S/M	1	1
6	F	0	1
5	E	0	1
4	D	0	1
3	C	0	1
2	B	0	1
1	Marron	0	1
8	Marron	1	1
\.


--
-- TOC entry 3384 (class 0 OID 18756)
-- Dependencies: 226
-- Data for Name: pagocredito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pagocredito (codcom, fecha, fecreg, monto, codusu, observacion) FROM stdin;
2	0020-11-30	2020-11-30 23:34:23.388737	150	1	Primer pago cuando se compra.
\.


--
-- TOC entry 3385 (class 0 OID 18762)
-- Dependencies: 227
-- Data for Name: pedido; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pedido (codped, fecha, estado, codusu, celular, direccion, observacion, fentrega, coddelivery, nit, razon_nit, codven, codsuc) FROM stdin;
\.


--
-- TOC entry 3386 (class 0 OID 18769)
-- Dependencies: 228
-- Data for Name: posicion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.posicion (codposicion, nombre, estado, codmueble) FROM stdin;
0	S/Posicion	1	0
11	Arriba	1	1
12	2	1	2
13	Arriba	1	8
14	Medio	1	8
15	Abajo	1	8
\.


--
-- TOC entry 3387 (class 0 OID 18773)
-- Dependencies: 229
-- Data for Name: presentacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.presentacion (codpre, nombre, estado) FROM stdin;
3	Bolsa	t
5	Caja	t
0	S/P	t
1	Pieza	t
2	Botella	t
7	Jaba	t
4	Paquete	t
10	Tambor	t
11	IBC	t
12	Bidon	t
13	Tineta	t
\.


--
-- TOC entry 3388 (class 0 OID 18779)
-- Dependencies: 230
-- Data for Name: proceso; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.proceso (codp, nombre, enlace, estado) FROM stdin;
14	Facturaciones	../dosificacion/gestion	1
10	Sucursales	../sucursal/gestion	1
17	Medidas	../medida/gestion	1
18	Tipo de mueble	../tipomueble/gestion	1
19	Mueble	../mueble/gestion	1
21	Ubicacion de productos	../lugar/gestion	1
20	Presentaciones	../presentacion/gestion	1
22	Cajas	../caja/gestion	1
12	Reportes	../reporte/gestion	1
24	Salidas	../salida/gestion	1
25	Mi caja	../caja/gestionVendedor	1
8	Mis ventas	../venta/gestion	1
26	Promocion	../promocion/gestion	1
28	Buscar kadex	../kardex/gestion	1
27	Buscador	../cliente/gestionBuscar	1
23	Inventario	../almacen/gestionInventario	1
31	Almacen	../almacen/gestionAlmacen	1
32	Reporte de Inventario	../almacen/reporteInventario	1
33	Cambiar clave	../usuario/acceso	1
34	Backups	../usuario/backup	1
36	Productos No Registrados	../producto_nuevo/gestion	1
39	Productos incompletos	../producto/gestionIncompletos	1
9	Clasificacion	../categoria/gestion	1
15	Sub Clasificacion	../tipo/gestion	1
38	Márgenes	../margen/gestion	0
35	Ejecucion Script	../sistema/script-sistema	0
37	Estandares Sistema	../template/form	0
16	Areas Empresa	../area/gestion	1
30	Prueba cli	../pedido/gestion_x_usuario	0
11	Marca Productos	../laboratorio/gestion	1
29	Prueba ped	../pedido/gestion_x_cliente	0
1	Registro de Usuarios	../usuario/gestion	1
2	Registro Productos	../producto/gestion	1
5	Registro Compras	../compra/gestion	1
3	Registro Proveedores	../proveedor/gestion	1
4	Registro Ventas	../venta/gestion	1
13	Registro Clientes	../cliente/gestion	1
6	Menu Sistema	../menu/gestion	1
7	Roles del Sistema	../rol/gestion	1
\.


--
-- TOC entry 3389 (class 0 OID 18783)
-- Dependencies: 231
-- Data for Name: producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto (codpro, nombre, estado, foto, codtip, generico, codigobarra, codlab, concentracion, codmed, codare, pc_unit, pv_unit, porcentaje_unidad, codpre, controlado, inventario_minimo_unidad, pareto, unixcaja, pv_caja, pv_descuento_caja, pc_caja, unixpaquete, uni_en_paquete, inventario_minimo_caja, inventario_minimo_paquete, pc_paquete, pv_paquete, porcentaje_caja, porcentaje_paquete, tipo_compra, pv_descuento_paquete, presentacion_unidad, presentacion_caja, presentacion_paquete, margen, porcentaje_descuento_caja, porcentaje_descuento_paquete) FROM stdin;
2	SIKA 3	1	producto.png	112	SIKA 3		1	6	11	3	150	153	2	\N	\N	5		1	153	\N	150	1	1	5	5	150	153	2	2	1	\N	Bidon	Bidon	Bidon	1	0	0
1	SIKA 3	1	producto.png	112	SIKA 3		1	1	11	3	100	102	2	\N	\N	5		1	102	\N	100	1	1	5	5	100	102	2	2	1	\N	Bidon	Bidon	Bidon	1	0	0
3	SIKA 3	1	producto.png	112	SIKA 3		1	20	11	3	211	221.6	5	\N	\N	4		1	221.6	\N	211	1	1	4	4	211	221.6	5	5	1	\N	Tineta	Tineta	Tineta	1	0	0
4	CINTA METRICA STANLEY 15M	1	producto-4.jpeg	133	CINTA METRICA 15M		5	1	5	4	3	3.3	10	\N	\N	3		1	3.3	\N	3	1	1	3	3	3	3.3	10	10	1	\N	Pieza	Pieza	Pieza	1	0	0
5	SIKA ACELERANTE	1	producto.png	112	SIKA ACELERANTE		1	2	11	3	100	102	2	\N	\N	21	\N	1	102	\N	100	1	1	21	21	100	102	2	2	1	\N	Bidon	Bidon	Bidon	\N	0	0
6	SIKA 3	1	producto.png	112	SIKA 3		1	1	11	3	25	27.5	10	\N	\N	15	\N	1	27.5	\N	25	1	1	15	15	25	27.5	10	10	1	\N	Botella	Botella	Botella	\N	0	0
\.


--
-- TOC entry 3390 (class 0 OID 18792)
-- Dependencies: 232
-- Data for Name: producto_nuevo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto_nuevo (cod_pro_nuevo, nombre, descripcion, estado, codusu) FROM stdin;
\.


--
-- TOC entry 3391 (class 0 OID 18796)
-- Dependencies: 233
-- Data for Name: promocion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.promocion (codpromo, fcreacion, fini, ffin, titulo, descripcion, codusu, estpromo, gestion) FROM stdin;
\.


--
-- TOC entry 3392 (class 0 OID 18802)
-- Dependencies: 234
-- Data for Name: proveedor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.proveedor (codproveedor, nombre, nit, direccion, telefono, estado) FROM stdin;
2	SIKA LA PAZ	123456789	La Paz Bolivia	65822033	1
\.


--
-- TOC entry 3393 (class 0 OID 18806)
-- Dependencies: 235
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rol (codr, nombre, estado, acceso_venta) FROM stdin;
3	ADMIN SYSTEM	1	t
1	ADMINISTRADOR	1	t
4	VENDEDOR	1	t
\.


--
-- TOC entry 3394 (class 0 OID 18811)
-- Dependencies: 236
-- Data for Name: rolme; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rolme (codr, codm) FROM stdin;
3	4
3	6
3	8
3	9
3	10
3	3
3	5
3	7
3	11
3	12
3	13
3	1
3	2
1	6
1	5
1	2
1	1
1	9
1	3
\.


--
-- TOC entry 3395 (class 0 OID 18814)
-- Dependencies: 237
-- Data for Name: rolusu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rolusu (codr, login) FROM stdin;
1	admin
3	admin
1	edserrudo
\.


--
-- TOC entry 3396 (class 0 OID 18817)
-- Dependencies: 238
-- Data for Name: salida; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.salida (codsal, fsalida, codusu, tipo, descripcion, estado, est, numero, suc_origen, suc_destino, in_out, solucion, monto, conclusion) FROM stdin;
1	2021-01-27	1	7	Actualizacion de almacen por rlaime	a	t	1	1	\N	t	6	\N	Aumento realizado por el usuario
\.


--
-- TOC entry 3397 (class 0 OID 18823)
-- Dependencies: 239
-- Data for Name: sucursal; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sucursal (codsuc, nombre, telefono, estado, direccion) FROM stdin;
2	Sucursal 1	cel: 71861315	t	Av. Froilán Tejerina esq. Suarez, frente aldeas SOS
1	Casa Matriz	cel: 71861314	t	Av. España N° 380, entre Delfín Pino y Av. Belgrano 
\.


--
-- TOC entry 3398 (class 0 OID 18829)
-- Dependencies: 240
-- Data for Name: tipo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tipo (codtip, nomtip, estado, codcat) FROM stdin;
99	Concreto	0	6
100	Impermeabilizacion	0	6
101	Sellado Y Pegado	0	6
102	Cubiertas Y Techos	0	6
103	Pisos Industriales	0	6
104	Pegado De Ceramicos Y Porcelanatos	0	6
105	Reparacion Y Rehabilitacion	0	6
106	Grouting Y Anclajes	0	6
107	Industria	0	6
108	Laboratorio	0	10
109	Medicion	0	10
110	Accesorios Hidraulicos	0	10
111	Articulos Varios	0	10
112	Acelerantes	1	11
113	Curado De Hormigon	1	11
114	Fluidificantes	1	11
115	Hormigon Impermeable	1	11
116	Plastificantes	1	11
117	Retardantes	1	11
118	Fibras	1	11
119	Impermeabilizantes	1	12
120	Juntas De Dilatacion	1	13
121	Sella Grietas Y Juntas	1	13
122	Siliconas	1	13
123	Sella Techos	1	14
124	Pisos Industriales	1	15
125	Adherente De Concreto	1	16
126	Adherente De Mortero	1	16
127	Morteros De Reparacion	1	16
128	Refuerzo Estructural	1	16
129	Anclaje Estructural	1	17
130	Inyecciones	1	17
131	Sellante Industrial	1	18
132	Pistolas De Aplicacion	1	18
133	Medicion	1	19
134	Laboratorio	1	19
135	Accesorios Hidraulicos	1	19
136	Articulos Varios	1	19
137	Topografia	1	19
\.


--
-- TOC entry 3399 (class 0 OID 18835)
-- Dependencies: 241
-- Data for Name: tipo_mueble; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tipo_mueble (codtimu, nombre, estado) FROM stdin;
0	S/M	t
1	Estante	t
\.


--
-- TOC entry 3400 (class 0 OID 18838)
-- Dependencies: 242
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (codusu, nombre, ap, am, genero, estado, tipoper, foto, ci, fnac, ecivil, alias) FROM stdin;
1	ROYETH	LAIME	FERNANDEZ	M	1	A	user.png		1990-12-25	Casado	rlaime
2	SIKA LA PAZ			M	1	P	notimage.png	\N	\N	\N	\N
5	ASDASDASDSA	ASDASDSADASD	\N	m	0	P	user.png	\N	\N	\N	\N
6	ROGER	WATERS	\N	m	0	P	user.png	\N	\N	\N	\N
4	CLIENTE PRIMERO	PRIMER	\N	m	0	P	user.png	\N	\N	\N	\N
3	JAIME	QUINTANILLA	\N	m	0	P	user.png	\N	\N	\N	\N
7	EDSON	SERRUDO	CH.	M	1	A	user.png	1234567	2020-12-12	Soltero	edserrudo
8	VENDEDOR	UNO	UNO	M	1	P	user.png	5798257	2020-12-25	Soltero	vendedor1
\.


--
-- TOC entry 3401 (class 0 OID 18843)
-- Dependencies: 243
-- Data for Name: venta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.venta (codven, fecha, estado, codcli, tiponota, codcaja, coddetcaja, formapago, total, codusu, total_pagado, cambio, codsuc) FROM stdin;
1	2020-12-01	1	0	1	1	1	1	108.6	1	200	91.4	1
\.


--
-- TOC entry 3086 (class 2606 OID 18878)
-- Name: acceso_sucursal acceso_sucursal_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acceso_sucursal
    ADD CONSTRAINT acceso_sucursal_pk PRIMARY KEY (codusu, codsuc, fecalta);


--
-- TOC entry 3092 (class 2606 OID 18880)
-- Name: almacen almacen6_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.almacen
    ADD CONSTRAINT almacen6_pk PRIMARY KEY (codalmacen);


--
-- TOC entry 3128 (class 2606 OID 18882)
-- Name: lugar almacen_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lugar
    ADD CONSTRAINT almacen_pk PRIMARY KEY (codlugar);


--
-- TOC entry 3094 (class 2606 OID 18884)
-- Name: area area_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.area
    ADD CONSTRAINT area_pk PRIMARY KEY (codare);


--
-- TOC entry 3096 (class 2606 OID 18886)
-- Name: backup backup_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.backup
    ADD CONSTRAINT backup_pkey PRIMARY KEY (cod);


--
-- TOC entry 3098 (class 2606 OID 18888)
-- Name: caja caja_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caja
    ADD CONSTRAINT caja_pkey PRIMARY KEY (codcaja);


--
-- TOC entry 3100 (class 2606 OID 18890)
-- Name: categoria categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (codcat);


--
-- TOC entry 3102 (class 2606 OID 18892)
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (codcli);


--
-- TOC entry 3104 (class 2606 OID 18894)
-- Name: compra compra_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.compra
    ADD CONSTRAINT compra_pkey PRIMARY KEY (codcom);


--
-- TOC entry 3106 (class 2606 OID 18896)
-- Name: cuenta cuenta_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cuenta
    ADD CONSTRAINT cuenta_pk PRIMARY KEY (codcuenta);


--
-- TOC entry 3108 (class 2606 OID 18898)
-- Name: detalle_margen detalle_margen_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_margen
    ADD CONSTRAINT detalle_margen_pkey PRIMARY KEY (cod_margen, number_margin);


--
-- TOC entry 3110 (class 2606 OID 18900)
-- Name: detalle_salida detalle_salida_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_salida
    ADD CONSTRAINT detalle_salida_pkey PRIMARY KEY (codsal, codalmacen);


--
-- TOC entry 3112 (class 2606 OID 18902)
-- Name: detallecaja detallecaja_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallecaja
    ADD CONSTRAINT detallecaja_pkey PRIMARY KEY (codcaja, coddetcaja);


--
-- TOC entry 3114 (class 2606 OID 18904)
-- Name: detallecompra detallecompra_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallecompra
    ADD CONSTRAINT detallecompra_pk PRIMARY KEY (codcom, coddcom);


--
-- TOC entry 3116 (class 2606 OID 18906)
-- Name: detallepedido detallepedido_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallepedido
    ADD CONSTRAINT detallepedido_pkey PRIMARY KEY (codped, codpro);


--
-- TOC entry 3118 (class 2606 OID 18908)
-- Name: detallepromo detallepromo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallepromo
    ADD CONSTRAINT detallepromo_pkey PRIMARY KEY (codpromo, codpro);


--
-- TOC entry 3120 (class 2606 OID 18910)
-- Name: detalleventa detalleventa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalleventa
    ADD CONSTRAINT detalleventa_pkey PRIMARY KEY (codpro, codven);


--
-- TOC entry 3122 (class 2606 OID 18912)
-- Name: dosificacion dosificacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dosificacion
    ADD CONSTRAINT dosificacion_pkey PRIMARY KEY (coddosificacion);


--
-- TOC entry 3124 (class 2606 OID 18914)
-- Name: factura factura_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factura
    ADD CONSTRAINT factura_pkey PRIMARY KEY (coddosificacion, numfac);


--
-- TOC entry 3126 (class 2606 OID 18916)
-- Name: laboratorio laboratorio_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.laboratorio
    ADD CONSTRAINT laboratorio_pk PRIMARY KEY (codlab);


--
-- TOC entry 3130 (class 2606 OID 18918)
-- Name: margen margen_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.margen
    ADD CONSTRAINT margen_pkey PRIMARY KEY (cod_margen);


--
-- TOC entry 3132 (class 2606 OID 18920)
-- Name: medida medida_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medida
    ADD CONSTRAINT medida_pk PRIMARY KEY (codmed);


--
-- TOC entry 3134 (class 2606 OID 18922)
-- Name: menu menus_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.menu
    ADD CONSTRAINT menus_pkey PRIMARY KEY (codm);


--
-- TOC entry 3136 (class 2606 OID 18924)
-- Name: mepro mepro_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mepro
    ADD CONSTRAINT mepro_pkey PRIMARY KEY (codm, codp);


--
-- TOC entry 3138 (class 2606 OID 18926)
-- Name: mueble mueble_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mueble
    ADD CONSTRAINT mueble_pkey PRIMARY KEY (codmueble);


--
-- TOC entry 3170 (class 2606 OID 18928)
-- Name: tipo_mueble newtable_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_mueble
    ADD CONSTRAINT newtable_pk PRIMARY KEY (codtimu);


--
-- TOC entry 3140 (class 2606 OID 18930)
-- Name: pagocredito pagocredito_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pagocredito
    ADD CONSTRAINT pagocredito_pkey PRIMARY KEY (codcom);


--
-- TOC entry 3142 (class 2606 OID 18932)
-- Name: pedido pedido_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido
    ADD CONSTRAINT pedido_pkey PRIMARY KEY (codped);


--
-- TOC entry 3172 (class 2606 OID 18934)
-- Name: usuario personas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT personas_pkey PRIMARY KEY (codusu);


--
-- TOC entry 3144 (class 2606 OID 18936)
-- Name: posicion posicion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posicion
    ADD CONSTRAINT posicion_pkey PRIMARY KEY (codposicion);


--
-- TOC entry 3146 (class 2606 OID 18938)
-- Name: presentacion presentacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.presentacion
    ADD CONSTRAINT presentacion_pkey PRIMARY KEY (codpre);


--
-- TOC entry 3148 (class 2606 OID 18940)
-- Name: proceso procesos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proceso
    ADD CONSTRAINT procesos_pkey PRIMARY KEY (codp);


--
-- TOC entry 3152 (class 2606 OID 18942)
-- Name: producto_nuevo producto_nuevo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto_nuevo
    ADD CONSTRAINT producto_nuevo_pkey PRIMARY KEY (cod_pro_nuevo);


--
-- TOC entry 3150 (class 2606 OID 18944)
-- Name: producto producto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_pkey PRIMARY KEY (codpro);


--
-- TOC entry 3154 (class 2606 OID 18946)
-- Name: promocion promocion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promocion
    ADD CONSTRAINT promocion_pkey PRIMARY KEY (codpromo);


--
-- TOC entry 3156 (class 2606 OID 18948)
-- Name: proveedor proveedor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proveedor
    ADD CONSTRAINT proveedor_pkey PRIMARY KEY (codproveedor);


--
-- TOC entry 3158 (class 2606 OID 18950)
-- Name: rol roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT roles_pkey PRIMARY KEY (codr);


--
-- TOC entry 3160 (class 2606 OID 18952)
-- Name: rolme rolme_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolme
    ADD CONSTRAINT rolme_pkey PRIMARY KEY (codr, codm);


--
-- TOC entry 3162 (class 2606 OID 18954)
-- Name: rolusu rolusu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolusu
    ADD CONSTRAINT rolusu_pkey PRIMARY KEY (codr, login);


--
-- TOC entry 3164 (class 2606 OID 18956)
-- Name: salida salida_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salida
    ADD CONSTRAINT salida_pkey PRIMARY KEY (codsal);


--
-- TOC entry 3166 (class 2606 OID 18958)
-- Name: sucursal sucursal_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sucursal
    ADD CONSTRAINT sucursal_pk PRIMARY KEY (codsuc);


--
-- TOC entry 3168 (class 2606 OID 18960)
-- Name: tipo tipo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo
    ADD CONSTRAINT tipo_pkey PRIMARY KEY (codtip);


--
-- TOC entry 3088 (class 2606 OID 18962)
-- Name: accesousuario usuarios_codper_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accesousuario
    ADD CONSTRAINT usuarios_codper_key UNIQUE (codusu);


--
-- TOC entry 3090 (class 2606 OID 18964)
-- Name: accesousuario usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accesousuario
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (login);


--
-- TOC entry 3174 (class 2606 OID 18966)
-- Name: venta venta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT venta_pkey PRIMARY KEY (codven);


--
-- TOC entry 3175 (class 2606 OID 18967)
-- Name: acceso_sucursal acceso_sucursal_sucursal_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acceso_sucursal
    ADD CONSTRAINT acceso_sucursal_sucursal_fk FOREIGN KEY (codsuc) REFERENCES public.sucursal(codsuc);


--
-- TOC entry 3176 (class 2606 OID 18972)
-- Name: acceso_sucursal acceso_sucursal_usuarios_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acceso_sucursal
    ADD CONSTRAINT acceso_sucursal_usuarios_fk FOREIGN KEY (codusu) REFERENCES public.usuario(codusu);


--
-- TOC entry 3178 (class 2606 OID 18977)
-- Name: almacen almacen_lugar_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.almacen
    ADD CONSTRAINT almacen_lugar_fk FOREIGN KEY (codlugar) REFERENCES public.lugar(codlugar);


--
-- TOC entry 3179 (class 2606 OID 18982)
-- Name: caja caja_codusu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caja
    ADD CONSTRAINT caja_codusu_fkey FOREIGN KEY (codusu) REFERENCES public.usuario(codusu);


--
-- TOC entry 3180 (class 2606 OID 18987)
-- Name: caja caja_sucursal_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.caja
    ADD CONSTRAINT caja_sucursal_fk FOREIGN KEY (codsuc) REFERENCES public.sucursal(codsuc);


--
-- TOC entry 3181 (class 2606 OID 18992)
-- Name: cliente cliente_codcli_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_codcli_fkey FOREIGN KEY (codcli) REFERENCES public.usuario(codusu);


--
-- TOC entry 3182 (class 2606 OID 18997)
-- Name: compra compra_codpro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.compra
    ADD CONSTRAINT compra_codpro_fkey FOREIGN KEY (codpro) REFERENCES public.proveedor(codproveedor);


--
-- TOC entry 3183 (class 2606 OID 19002)
-- Name: compra compra_codusu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.compra
    ADD CONSTRAINT compra_codusu_fkey FOREIGN KEY (codusu) REFERENCES public.usuario(codusu);


--
-- TOC entry 3184 (class 2606 OID 19007)
-- Name: compra compra_detallecaja_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.compra
    ADD CONSTRAINT compra_detallecaja_fk FOREIGN KEY (codcaja, coddetcaja) REFERENCES public.detallecaja(codcaja, coddetcaja);


--
-- TOC entry 3185 (class 2606 OID 19012)
-- Name: detalle_margen detalle_margen_cod_margen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_margen
    ADD CONSTRAINT detalle_margen_cod_margen_fkey FOREIGN KEY (cod_margen) REFERENCES public.margen(cod_margen);


--
-- TOC entry 3186 (class 2606 OID 19017)
-- Name: detalle_salida detalle_salida_codsal_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_salida
    ADD CONSTRAINT detalle_salida_codsal_fkey FOREIGN KEY (codsal) REFERENCES public.salida(codsal);


--
-- TOC entry 3187 (class 2606 OID 19022)
-- Name: detallecaja detallecaja_codcaja_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallecaja
    ADD CONSTRAINT detallecaja_codcaja_fkey FOREIGN KEY (codcaja) REFERENCES public.caja(codcaja);


--
-- TOC entry 3188 (class 2606 OID 19027)
-- Name: detallecaja detallecaja_cuenta_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallecaja
    ADD CONSTRAINT detallecaja_cuenta_fk FOREIGN KEY (codcuenta) REFERENCES public.cuenta(codcuenta);


--
-- TOC entry 3189 (class 2606 OID 19032)
-- Name: detallecompra detallecompra_almacen_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallecompra
    ADD CONSTRAINT detallecompra_almacen_fk FOREIGN KEY (codalmacen) REFERENCES public.almacen(codalmacen);


--
-- TOC entry 3190 (class 2606 OID 19037)
-- Name: detallecompra detallecompra_codcom_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallecompra
    ADD CONSTRAINT detallecompra_codcom_fkey FOREIGN KEY (codcom) REFERENCES public.compra(codcom);


--
-- TOC entry 3191 (class 2606 OID 19042)
-- Name: detallepedido detallepedido_codped_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallepedido
    ADD CONSTRAINT detallepedido_codped_fkey FOREIGN KEY (codped) REFERENCES public.pedido(codped);


--
-- TOC entry 3192 (class 2606 OID 19047)
-- Name: detallepromo detallepromo_codpromo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detallepromo
    ADD CONSTRAINT detallepromo_codpromo_fkey FOREIGN KEY (codpromo) REFERENCES public.promocion(codpromo);


--
-- TOC entry 3193 (class 2606 OID 19052)
-- Name: detalleventa detalleventa_codv_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalleventa
    ADD CONSTRAINT detalleventa_codv_fkey FOREIGN KEY (codven) REFERENCES public.venta(codven);


--
-- TOC entry 3194 (class 2606 OID 19057)
-- Name: dosificacion dosificacion_sucursal_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dosificacion
    ADD CONSTRAINT dosificacion_sucursal_fk FOREIGN KEY (codsuc) REFERENCES public.sucursal(codsuc);


--
-- TOC entry 3195 (class 2606 OID 19062)
-- Name: factura factura_ibfk_1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factura
    ADD CONSTRAINT factura_ibfk_1 FOREIGN KEY (coddosificacion) REFERENCES public.dosificacion(coddosificacion);


--
-- TOC entry 3196 (class 2606 OID 19067)
-- Name: lugar lugar_posicion_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lugar
    ADD CONSTRAINT lugar_posicion_fk FOREIGN KEY (codposicion) REFERENCES public.posicion(codposicion);


--
-- TOC entry 3197 (class 2606 OID 19072)
-- Name: lugar lugar_producto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lugar
    ADD CONSTRAINT lugar_producto_fk FOREIGN KEY (codpro) REFERENCES public.producto(codpro);


--
-- TOC entry 3198 (class 2606 OID 19077)
-- Name: lugar lugar_sucursal_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lugar
    ADD CONSTRAINT lugar_sucursal_fk FOREIGN KEY (codsuc) REFERENCES public.sucursal(codsuc);


--
-- TOC entry 3199 (class 2606 OID 19082)
-- Name: margen margen_codusu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.margen
    ADD CONSTRAINT margen_codusu_fkey FOREIGN KEY (codusu) REFERENCES public.usuario(codusu);


--
-- TOC entry 3200 (class 2606 OID 19087)
-- Name: mepro mepro_codm_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mepro
    ADD CONSTRAINT mepro_codm_fkey FOREIGN KEY (codm) REFERENCES public.menu(codm);


--
-- TOC entry 3201 (class 2606 OID 19092)
-- Name: mepro mepro_codp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mepro
    ADD CONSTRAINT mepro_codp_fkey FOREIGN KEY (codp) REFERENCES public.proceso(codp);


--
-- TOC entry 3202 (class 2606 OID 19097)
-- Name: mueble mueble_tipo_mueble_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mueble
    ADD CONSTRAINT mueble_tipo_mueble_fk FOREIGN KEY (codtimu) REFERENCES public.tipo_mueble(codtimu);


--
-- TOC entry 3203 (class 2606 OID 19102)
-- Name: pagocredito pagocredito_codcom_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pagocredito
    ADD CONSTRAINT pagocredito_codcom_fkey FOREIGN KEY (codcom) REFERENCES public.compra(codcom);


--
-- TOC entry 3204 (class 2606 OID 19107)
-- Name: pedido pedido_codusu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pedido
    ADD CONSTRAINT pedido_codusu_fkey FOREIGN KEY (codusu) REFERENCES public.usuario(codusu);


--
-- TOC entry 3205 (class 2606 OID 19112)
-- Name: posicion posicion_mueble_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posicion
    ADD CONSTRAINT posicion_mueble_fk FOREIGN KEY (codmueble) REFERENCES public.mueble(codmueble);


--
-- TOC entry 3206 (class 2606 OID 19117)
-- Name: producto producto_area_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_area_fk FOREIGN KEY (codare) REFERENCES public.area(codare);


--
-- TOC entry 3207 (class 2606 OID 19122)
-- Name: producto producto_codtip_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_codtip_fkey FOREIGN KEY (codtip) REFERENCES public.tipo(codtip);


--
-- TOC entry 3208 (class 2606 OID 19127)
-- Name: producto producto_laboratorio_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_laboratorio_fk FOREIGN KEY (codlab) REFERENCES public.laboratorio(codlab);


--
-- TOC entry 3209 (class 2606 OID 19132)
-- Name: producto producto_medida_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_medida_fk FOREIGN KEY (codmed) REFERENCES public.medida(codmed);


--
-- TOC entry 3210 (class 2606 OID 19137)
-- Name: producto producto_presentacion_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_presentacion_fk FOREIGN KEY (codpre) REFERENCES public.presentacion(codpre);


--
-- TOC entry 3211 (class 2606 OID 19142)
-- Name: promocion promocion_codusu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.promocion
    ADD CONSTRAINT promocion_codusu_fkey FOREIGN KEY (codusu) REFERENCES public.usuario(codusu);


--
-- TOC entry 3212 (class 2606 OID 19147)
-- Name: proveedor proveedor_codproveedor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proveedor
    ADD CONSTRAINT proveedor_codproveedor_fkey FOREIGN KEY (codproveedor) REFERENCES public.usuario(codusu);


--
-- TOC entry 3213 (class 2606 OID 19152)
-- Name: rolme rolme_codm_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolme
    ADD CONSTRAINT rolme_codm_fkey FOREIGN KEY (codm) REFERENCES public.menu(codm);


--
-- TOC entry 3214 (class 2606 OID 19157)
-- Name: rolme rolme_rolme_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolme
    ADD CONSTRAINT rolme_rolme_fkey FOREIGN KEY (codr) REFERENCES public.rol(codr);


--
-- TOC entry 3215 (class 2606 OID 19162)
-- Name: rolusu rolusu_codr_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolusu
    ADD CONSTRAINT rolusu_codr_fkey FOREIGN KEY (codr) REFERENCES public.rol(codr);


--
-- TOC entry 3216 (class 2606 OID 19167)
-- Name: rolusu rolusu_login_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolusu
    ADD CONSTRAINT rolusu_login_fkey FOREIGN KEY (login) REFERENCES public.accesousuario(login);


--
-- TOC entry 3217 (class 2606 OID 19172)
-- Name: salida salida_codusu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salida
    ADD CONSTRAINT salida_codusu_fkey FOREIGN KEY (codusu) REFERENCES public.usuario(codusu);


--
-- TOC entry 3218 (class 2606 OID 19177)
-- Name: salida salida_suc_destino_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salida
    ADD CONSTRAINT salida_suc_destino_fkey FOREIGN KEY (suc_destino) REFERENCES public.sucursal(codsuc);


--
-- TOC entry 3219 (class 2606 OID 19182)
-- Name: salida salida_suc_origen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salida
    ADD CONSTRAINT salida_suc_origen_fkey FOREIGN KEY (suc_origen) REFERENCES public.sucursal(codsuc);


--
-- TOC entry 3220 (class 2606 OID 19187)
-- Name: tipo tipo_codcat_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo
    ADD CONSTRAINT tipo_codcat_fkey FOREIGN KEY (codcat) REFERENCES public.categoria(codcat);


--
-- TOC entry 3177 (class 2606 OID 19192)
-- Name: accesousuario usuarios_codusu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accesousuario
    ADD CONSTRAINT usuarios_codusu_fkey FOREIGN KEY (codusu) REFERENCES public.usuario(codusu);


--
-- TOC entry 3221 (class 2606 OID 19197)
-- Name: venta venta_codcaja_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT venta_codcaja_fkey FOREIGN KEY (codcaja, coddetcaja) REFERENCES public.detallecaja(codcaja, coddetcaja);


-- Completed on 2021-01-29 19:40:19

--
-- PostgreSQL database dump complete
--

