package suiteVideoconsulta;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import components.Hook;
import steps.D_EnvioInvitacion_MobileStep;
import steps.M_AtenderSocioStep;
import steps.M_EntrarVC_IngresarStep;

public class Regresion_Videoconsulta extends Hook {

	// Suite de videoconsultas
	
	@Test
	public void M_AtenderVC_01(Method method) {
		M_EntrarVC_IngresarStep.Run(method.getName());
	}

	@Test (dependsOnMethods = {"M_AtenderVC_01"})
	public void VC_GenerarInvitacion_01(Method method) {
		D_EnvioInvitacion_MobileStep.Run(method.getName());
	}

	@Test (dependsOnMethods = {"VC_GenerarInvitacion_01"})
	public void VC_AtenderSocio_01(Method method) {
		M_AtenderSocioStep.Run(method.getName());
	}
	
	
}
