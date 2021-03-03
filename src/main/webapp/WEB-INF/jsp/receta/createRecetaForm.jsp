<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Crear Receta">
		<jsp:attribute name="customScript">
	        <script>
	            $(function () {
	                $("#fecha").datepicker();*/
	            });
	        </script>
	</jsp:attribute>
	<jsp:body>		        	

		<form:form modelAttribute="recetaForm" class="form-horizontal" id="create-receta-form">
		        <div class="form-group has-feedback">
		        <div>
		        <c:if test="${cantidadError != null}"><c:out value="${cantidadError}"/></c:if>
		        </div>	        		 
				<petclinic:inputField label="Descripcion" name="descripcion"/>
				
				<label>Cita a la que asociar la receta:</label>
				<select id="citaId" name="citaId" required>
                <c:forEach items="${citas}" var="citasmod">
                <option value="${citasmod.id}"><c:out value="${citasmod.fechaHoraInicio} con ${citasmod.contrato.explotacionGanadera.ganadero.firstName} ${citasmod.contrato.explotacionGanadera.ganadero.lastName}"/></option>
                </c:forEach>
            	</select><br></br>
				
				<label>Productos</label>
            	<c:forEach items="${productos}" var="producto">
            	</br>
            	<tr>
				<td><b>${producto.name }</b></td></br>
				<td>Precio: ${producto.precio} 	euros</td></br>
				<c:if test = "${producto.cantidad == 0}">
         		<tr>Producto fuera de stock</tr>
      			</c:if>
      			<c:if test = "${producto.cantidad > 0}">
      			<form:hidden path="productoId" value="${producto.id}"/>
      			
            	<td><select id="cantidad" name="cantidad" required size=6>
				<option value="0" selected><c:out value = "0"/></option>
            	<c:forEach var = "i" begin = "1" end = "${producto.cantidad}">
				<option value="${i}"><c:out value = "${i}"/></option>
				</c:forEach>
				</select></td>
				</c:if>
				</tr>
				</br>
            	</c:forEach>
            	<br></br>			
            	
		        </div>
		        <div class="form-group">
		            <div class="col-sm-offset-2 col-sm-10">
		                        <button class="btn btn-default" type="submit">Crear receta</button>
		            </div>
		        </div>
		    </form:form>
	</jsp:body>
</petclinic:layout>