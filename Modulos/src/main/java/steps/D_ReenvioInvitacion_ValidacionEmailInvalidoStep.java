package steps;

import static config.DataSetter.configuration;

import java.io.IOException;

import org.testng.Assert;

import base.BaseStep;
import context.Invitaciones;
import context.Usuario;
import helpers.ExcelHelper;
import helpers.PageHelper;
import helpers.StepHelper;
import helpers.XMLHelper;
import pages.D_Envio_InvitacionesPage;
import pages.D_HomePage;
import pages.D_LoginPage;

public class D_ReenvioInvitacion_ValidacionEmailInvalidoStep extends BaseStep {

	public static void Run(String testName) {
		String nombrePaso = "";
		try {
			log.Log.startTestCase(testName);
			Usuario usr = XMLHelper.object.getUsuario();
			
			try {
				ExcelHelper.leerExcelCargado(testName);
			} catch (IOException e) {
				log.Log.info(e.getMessage());
				log.Log.FailStep("error al leer el excel");
				Assert.fail("Fallo test - " + testName + " - Step: " + "Carga de datos");
			}
			
			Invitaciones inv = ExcelHelper.objectExcel.getInvitacion();
			PageHelper.deleteAllCookies(driver);

			NavigateToSite(configuration.getUrlPrestadores());
			CurrentPage = (new D_LoginPage().GetInstance(D_LoginPage.class));
			StepHelper.takeScreenShot(testName);

			nombrePaso = "1_Se ingresa a Login Despacho";
			CurrentPage.As(D_LoginPage.class).ingresarUsuario(usr.getEmail());
			CurrentPage.As(D_LoginPage.class).ingresarContrasenia( usr.getPassword());
			CurrentPage.As(D_LoginPage.class).clicBtnIngresar();
			StepHelper.takeScreenShot(testName);
			log.Log.SuccessStep(nombrePaso);

			nombrePaso = "2_Se ingresa al modulo invitaciones";
			CurrentPage = (new D_HomePage().GetInstance(D_HomePage.class));
			CurrentPage.As(D_HomePage.class).ingresarModuloInvitaciones();
			CurrentPage = (new D_Envio_InvitacionesPage().GetInstance(D_Envio_InvitacionesPage.class));
			Assert.assertTrue(CurrentPage.As(D_Envio_InvitacionesPage.class).estaHabilitadoBtnInvitaciones());
			log.Log.SuccessStep(nombrePaso);

			nombrePaso = "3_Se hace clik en reenviar invitacion";
			Assert.assertTrue(CurrentPage.As(D_Envio_InvitacionesPage.class).abrirFormReenviarInvitacion(inv.getNroSocio()));	
			log.Log.SuccessStep(nombrePaso);
			
			nombrePaso = "4_Se modifica los campos email y telefono";
			CurrentPage.As(D_Envio_InvitacionesPage.class).cargarEmailReenvio(inv.getEmail());	
			CurrentPage.As(D_Envio_InvitacionesPage.class).cargarTelefonoReenvio(inv.getNroCelular());	
			log.Log.SuccessStep(nombrePaso);

			if (inv.getEmail().isEmpty()) {
				nombrePaso = "5_Se verifica texto error de campos Obligatorios - email";
				Assert.assertEquals(CurrentPage.As(D_Envio_InvitacionesPage.class).obtenerErrorReenvioLabelEmail(),"El campo es requerido");	
				log.Log.SuccessStep(nombrePaso);
			}
			
			if (inv.getEmail().length() < 5 && !inv.getEmail().isEmpty() ) {
				nombrePaso = "5_Se verifica el mensaje de error del campo: email - longitud menor";
				Assert.assertEquals(CurrentPage.As(D_Envio_InvitacionesPage.class).obtenerErrorReenvioLabelEmail(),"El campo es muy corto");	
				log.Log.SuccessStep(nombrePaso);	
			}
			
			if (inv.getEmail().contains("+")) {
				nombrePaso = "5_Se verifica el mensaje de error del campo: email - no v�lido - posee caracteres especiales";
				Assert.assertEquals(CurrentPage.As(D_Envio_InvitacionesPage.class).obtenerErrorReenvioLabelEmail(),"El campo debe ser un email v�lido");	
				log.Log.SuccessStep(nombrePaso);	
			}
			
			if (!inv.getEmail().contains("@") && !inv.getEmail().isEmpty()) {
				nombrePaso = "5_Se verifica el mensaje de error del campo: email - no v�lido - no contiene @";
				Assert.assertEquals(CurrentPage.As(D_Envio_InvitacionesPage.class).obtenerErrorReenvioLabelEmail(),"El campo debe ser un email v�lido");	
				log.Log.SuccessStep(nombrePaso);	
			}
			
			if (inv.getEmail().contains("@..@")) {
				nombrePaso = "5_Se verifica el mensaje de error del campo: email - no v�lido - contiene @..@";
				Assert.assertEquals(CurrentPage.As(D_Envio_InvitacionesPage.class).obtenerErrorReenvioLabelEmail(),"El campo debe ser un email v�lido");	
				log.Log.SuccessStep(nombrePaso);	
			}
			
			if (inv.getEmail().contains("123(abc)@gmail")) {
				nombrePaso = "5_Se verifica el mensaje de error del campo: email - no v�lido - caracteres no permitidos";
				Assert.assertEquals(CurrentPage.As(D_Envio_InvitacionesPage.class).obtenerDatoEmail(),"123abc@gmail");	
				log.Log.SuccessStep(nombrePaso);	
			}
			
			if (inv.getEmail().contains("@com.")) {
				nombrePaso = "5_Se verifica el mensaje de error del campo: email - no v�lido - punto al final";
				Assert.assertEquals(CurrentPage.As(D_Envio_InvitacionesPage.class).obtenerErrorReenvioLabelEmail(),"El campo debe ser un email v�lido");	
				log.Log.SuccessStep(nombrePaso);	
			}
			
			if (inv.getEmail().contains("12345678")) {
				nombrePaso = "5_Se verifica el mensaje de error del campo: email - no v�lido - solo valores num�ricos";
				Assert.assertEquals(CurrentPage.As(D_Envio_InvitacionesPage.class).obtenerErrorReenvioLabelEmail(),"El campo debe ser un email v�lido");	
				log.Log.SuccessStep(nombrePaso);	
			}

			StepHelper.takeScreenShot(testName);

		} catch (AssertionError ex) {
			log.Log.info(ex.getMessage());
			log.Log.FailStep(nombrePaso);
			Assert.fail("Fallo test - " + testName + " - Step: " + nombrePaso);
		}

		log.Log.endTestCase(testName);	
	}


}
