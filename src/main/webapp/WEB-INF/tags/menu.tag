<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>


<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<petclinic:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Inicio</span>
				</petclinic:menuItem>

                <!-- Contratar veterinario (Ganadero) -->
                <sec:authorize access="hasAnyAuthority('ganadero')">
				<petclinic:menuItem active="${name eq 'contrato'}" url="/veterinario/encontrar"
					title="Contratar veterinario">
					<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>
					<span>Contratar</span>
				</petclinic:menuItem>
				</sec:authorize>

                <!-- Mis explotaciones ganaderas (Ganadero) -->
				<sec:authorize access="hasAnyAuthority('ganadero')">
				<petclinic:menuItem active="${name eq 'expGanaderas'}" url="/ganadero/explotacion-ganadera/list"
					title="Ver mis explotaciones ganaderas">
					<span class="glyphicon glyphicon-grain" aria-hidden="true"></span>
					<span>Explotaciones</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<!-- Contratos (Veterinario) -->
				<sec:authorize access="hasAnyAuthority('veterinario')">
				<petclinic:menuItem active="${name eq 'contrato'}" url="/contrato/list"
					title="Ver contratos">
					<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>
					<span>Contratos</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<!-- Mis explotaciones ganaderas (Veterinario) -->
				<sec:authorize access="hasAnyAuthority('veterinario')">
				<petclinic:menuItem active="${name eq 'expGanaderas'}" url="/veterinario/explotacion-ganadera/list"
					title="Ver mis explotaciones ganaderas">
					<span class="glyphicon glyphicon-grain" aria-hidden="true"></span>
					<span>Explotaciones</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<!-- Citas (Ganadero) -->
				<sec:authorize access="hasAnyAuthority('ganadero')">
				<petclinic:menuItem active="${name eq 'citas'}" url="/ganadero/cita/list"
					title="Ver mis citas">
					<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
					<span>Citas</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<!-- Citas (Veterinario) -->
				<sec:authorize access="hasAnyAuthority('veterinario')">
				<petclinic:menuItem active="${name eq 'citas'}" url="/veterinario/cita/list"
					title="Ver mis citas">
					<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span>
					<span>Citas</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<!-- Recetas (Ganadero) -->
				<sec:authorize access="hasAnyAuthority('ganadero')">
				<petclinic:menuItem active="${name eq 'recetas'}" url="/ganadero/receta/list"
					title="Ver mi recetario">
					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
					<span>Recetario</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<!-- Recetas (Veterinario) -->
				<sec:authorize access="hasAnyAuthority('veterinario')">
				<petclinic:menuItem active="${name eq 'recetas'}" url="/veterinario/receta/list"
					title="Ver mi recetario">
					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
					<span>Recetario</span>
				</petclinic:menuItem>
				</sec:authorize>
				
					<sec:authorize access="hasAnyAuthority('ganadero')">
				<petclinic:menuItem active="${name eq 'facturas'}" url="/ganadero/factura/list"
					title="Ver mis facturas">
					<span class="glyphicon glyphicon-credit-card" aria-hidden="true"></span>
					<span>Facturas</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAnyAuthority('veterinario')">
				<petclinic:menuItem active="${name eq 'facturas'}" url="/factura/select"
					title="Ver facturas de mis clientes">
					<span class="glyphicon glyphicon-credit-card" aria-hidden="true"></span>
					<span>Facturas</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAnyAuthority('veterinario')">
				<petclinic:menuItem active="${name eq 'productos'}" url="/producto/list"
					title="Ver mi almacÃ©n">
					<span class="glyphicon glyphicon-briefcase" aria-hidden="true"></span>
					<span><fmt:message key="Almacen"/></span>
				</petclinic:menuItem>
				</sec:authorize>
				<!--  
				<petclinic:menuItem active="${name eq 'vets'}" url="/vets"
					title="veterinarians">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Por ahora esto no hace nada</span>
				</petclinic:menuItem>
-->
				<petclinic:menuItem active="${name eq 'error'}" url="/oups"
					title="trigger a RuntimeException to see how it is handled">
					<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
					<span>Error</span>
				</petclinic:menuItem>

			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Loguearse</a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> 
							<strong>Registrate</strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
						</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-8">
											<p class="text-center">
												<a href="<c:url value="/ganadero/new" />"
													class="btn btn-primary btn-block btn-sm">Ganadero</a>
											</p>
											<p>				</p>
											<p class="text-center">
												<a href="<c:url value="/veterinario/new" />"
													class="btn btn-primary btn-block btn-sm">Veterinario</a>
											</p>
										</div>
									</div>
								</div>
							</li>
						</ul>
						<!--  -->
						
					<!-- <li><a href="<c:url value="/users/new" />">Registrate</a></li> -->
					
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change
													Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
