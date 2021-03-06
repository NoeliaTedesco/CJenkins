package steps;

import static config.DataSetter.configuration;

import base.BaseStep;
import context.Usuario;
import helpers.PageHelper;
import helpers.StepHelper;
import helpers.XMLHelper;
import junit.framework.Assert;
import pages.M_AtenderVideoconsultasPage;
import pages.M_HomePage;
import pages.M_LoginPage;

public class M_EntrarVC_SalirGuardiaStep extends BaseStep {
	
	public static void Run(String testName) {
		String nombrePaso = "";
		try {
			log.Log.startTestCase(testName);
			Usuario usr = XMLHelper.object.getUsuario();
				
			PageHelper.deleteAllCookies(driver);
			nombrePaso = "1_Se loguea el medico";
			NavigateToSite(configuration.getUrlMedico());
			CurrentPage = (new M_LoginPage().GetInstance(M_LoginPage.class));
			StepHelper.takeScreenShot(testName);
			
			CurrentPage.As(M_LoginPage.class).ingresarEmail(usr.getEmail());
			CurrentPage.As(M_LoginPage.class).ingresarContrasenia(usr.getPassword());
			CurrentPage.As(M_LoginPage.class).clicBtnIngresar();
			log.Log.SuccessStep(nombrePaso);
			
			nombrePaso = "2_Se ingresa a la home del medico";
			CurrentPage = (new M_HomePage().GetInstance(M_HomePage.class));
			Assert.assertTrue(CurrentPage.As(M_HomePage.class).esVisibleMenuAtenderVideoconsultas());
			log.Log.SuccessStep(nombrePaso);

			nombrePaso = "3_Se ingresa al modulo Atender videoconsultas";
			CurrentPage.As(M_HomePage.class).ingresarModuloAtenderVideoconsultas();
			CurrentPage = (new M_AtenderVideoconsultasPage().GetInstance(M_AtenderVideoconsultasPage.class));
			CurrentPage.As(M_AtenderVideoconsultasPage.class).esVisiblePanelInformacion();
			log.Log.SuccessStep(nombrePaso);
			StepHelper.takeScreenShot(testName);
		
			if (CurrentPage.As(M_AtenderVideoconsultasPage.class).esVisibleBotonEntrarGuardia()) { 
				nombrePaso = "3_B_Se ingresa a la guardia";
				CurrentPage.As(M_AtenderVideoconsultasPage.class).clicBtnIngresarGuardia();
				Assert.assertTrue(CurrentPage.As(M_AtenderVideoconsultasPage.class).esVisibleBotonSalirGuardia());
				StepHelper.takeScreenShot(testName);
				log.Log.SuccessStep(nombrePaso);
			}
			
			nombrePaso = "4_Se hace clic en el boton Salir de la guardia";
			Assert.assertTrue(CurrentPage.As(M_AtenderVideoconsultasPage.class).esVisibleBotonSalirGuardia());
			CurrentPage.As(M_AtenderVideoconsultasPage.class).clicBtnSalirGuardia();
			StepHelper.takeScreenShot(testName);
			log.Log.SuccessStep(nombrePaso);
			
			nombrePaso = "5_Visualizar home del medico";
			CurrentPage = (new M_HomePage().GetInstance(M_HomePage.class));
			Assert.assertTrue(CurrentPage.As(M_HomePage.class).esVisibleMenuAtenderVideoconsultas());
			StepHelper.takeScreenShot(testName);
			log.Log.SuccessStep(nombrePaso);
					
		} catch (AssertionError ex) {
			log.Log.info(ex.getMessage());
			log.Log.FailStep(nombrePaso);
			Assert.fail("Fallo test - " + testName + " - Step: " + nombrePaso);
		}
		log.Log.endTestCase(testName);	
	}

}
