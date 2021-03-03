<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Crear Factura">

<script type ="text/javascript">

    function dividirCadena(cadenaADividir,separador) {
	   var arrayDeCadenas = cadenaADividir.split(separador);
	   return arrayDeCadenas;
	}

	function anadirProducto() {
		
       valorSeleccionado = document.getElementById('productosAAnadir').value;
		
       if(valorSeleccionado != "" && document.getElementById('cantidadAAnadir').value != ""){
	   var coma = ",";
	   var arraySpliteado = dividirCadena(valorSeleccionado,coma);
	   	
	   select = document.getElementById('productoId');
	   var opt = document.createElement('option');   
	   opt.value = arraySpliteado[0];
	   opt.setAttribute("selected","");
	   select.appendChild(opt);
	   
	   select2 = document.getElementById('cantidad');
	   var opt2 = document.createElement('option');
	   opt2.value = document.getElementById('cantidadAAnadir').value;
	   opt2.setAttribute("selected","");
	   select2.appendChild(opt2);
	   
	   var seleccionado = document.getElementById('productosAAnadir');
	   
	   var newt = document.createElement("div");
	   var t = document.createTextNode('Cantidad de '+ seleccionado.options[seleccionado.selectedIndex].text +': '+ document.getElementById('cantidadAAnadir').value);       
	   newt.appendChild(t);                                         
	   document.getElementById("productosElegidos").appendChild(newt); 
	   
	   seleccionado.remove(seleccionado.selectedIndex);
	   document.getElementById('cantidadAAnadir').value = "";
	   
	   cargarCantidadProductoSeleccionado();
       }
	}
	
	function cargarCantidadProductoSeleccionado() {
		
		valorSeleccionado = document.getElementById('productosAAnadir').value;
		
		var coma = ",";
		var arraySpliteado = dividirCadena(valorSeleccionado,coma);
		
	    cantidadAAnadir = document.getElementById('cantidadAAnadir');
		cantidadAAnadir.setAttribute("max", arraySpliteado[1]);
		cantidadAAnadir.value = "";
	}
</script>
		<c:if test="${message != null}"><c:out value="${message}"/></c:if>
		        	
		<h1>Nueva Factura</h1>	
		<form:form modelAttribute="factura" class="form-horizontal" id="create-factura">
		        <div class="form-group has-feedback">
		    
		    <h2><label>Recetas</label></h2>
		    <c:choose>       
		    <c:when test="${!empty recetas}">
		    <div id="divRecetas">
					<label>Recetas pendientes de pago</label>
			<select id="recetasId" name="recetas" multiple>
			<c:forEach items="${recetas}" var="recetas">
			<option value="${recetas.id}"><c:out value="${recetas.fechaRealizacion}"/></option>
			</c:forEach>		
			</select>
			</div>
			</c:when>
			<c:otherwise>
			No tienes recetas pendientes de pago.
			</c:otherwise>
			</c:choose>
			
			<br></br>
			
			<h2><label>Productos</label></h2>
			<c:choose>
			<c:when test="${!empty productos}">
			<div id="divProductos">
			<label>Productos añadidos:</label>
			<div id="productosElegidos"></div>
			<select id="productosAAnadir" name="productosAAnadir"  onchange="cargarCantidadProductoSeleccionado();">
		        <option value="" selected disabled>Eliga el producto a añadir</option>
		        <c:forEach items="${productos}" var="productos">
			    <option value="${productos.id},${productos.cantidad}" ><c:out value="${productos.name}"/></option>
			    </c:forEach>
		    </select>
		    <input type="number" id="cantidadAAnadir" name="cantidadAAnadir" min="1" step="1" />
		    <input type="button" value="Añadir producto" onclick="anadirProducto();"/>
	
			
			<select id="productoId" name="productoId" style="visibility:hidden" multiple>
            </select>
            <select id="cantidad" name="cantidad" style="visibility:hidden" multiple>
            </select>
            </div>
            </c:when>
            <c:otherwise>
            No tienes productos existentes o productos con existencias. Ve a la pestaña "Almacén" si quiere añadir un producto.
            </c:otherwise>
            </c:choose> 			

				</div>
		        <div class="form-group">
		            <div class="col-sm-offset-2 col-sm-10">
		                        <button class="btn btn-default" type="submit">Crear factura</button>
		            </div>
		        </div>
		    </form:form>
		    
</petclinic:layout>