package Aspectos;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
@Component
@Aspect
public class SessionControl{
	
	/*@Around("execution(Map<?> Controladores.*C.*(..))")
	public @ResponseBody Map<?, ?> sessionCheck(ProceedingJoinPoint p) throws Throwable{
		Map<String, Object> Data = new HashMap<String, Object>();
		HttpServletRequest request=(HttpServletRequest)p.getArgs()[0];
		System.out.println("map");
		if(request.getSession().getAttribute("us")==null)
			Data.put("estatus", "error");
		else
			p.proceed().toString();
		
		return Data;
		
	}*/
	
	@Around("execution(String Controladores.*C.*(..))")
	public String sessionCheckS(ProceedingJoinPoint p){
		HttpServletRequest request=(HttpServletRequest)p.getArgs()[0];
		if(request.getSession().getAttribute("us")==null)return "redirect:../principal/sessionexpirada";
		try {
			return p.proceed().toString();
		} catch (Throwable e) {
			e.printStackTrace();
			return "redirect:../principal/sessionexpirada";
		}
	}
}
