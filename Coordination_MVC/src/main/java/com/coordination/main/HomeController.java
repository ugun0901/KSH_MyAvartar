package com.coordination.main;

import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coordination.dao.StyleDAO;
import com.coordination.dto.StyleVO;
import com.coordination.weather.ApiExplorerWeather;
import com.coordination.weather.CoordFetcher;
import com.google.gson.JsonArray;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private StyleDAO styleDAO;
	
	private String top="경기도";
	private String mdl="부천시소사구";
	private String leaf="괴안동";
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws Exception 
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(@ModelAttribute StyleVO vo, Model model, Locale locale) throws Exception {
		logger.info("패션 코디북, 오늘 뭐 입지??");
		
		top="경기도";
		mdl="부천시소사구";
		leaf="괴안동";
		
		
		return "coordination/index";
	}
	
	// Ajax 지역선택 처리 매핑
	@ResponseBody // view가 아닌 data리턴
    @RequestMapping(value="/weather/selTop", method=RequestMethod.POST, produces="text/plain;charset=utf-8")
    public String selTop(@RequestParam("selTop")String selTop) throws Exception {
        logger.info("selTop : "+selTop);
        CoordFetcher coord = new CoordFetcher();
        JSONArray mapMdl = coord.getMapMdl(selTop);
        //System.out.println(mapMdl.toJSONString());
        top=selTop;
        return mapMdl.toJSONString();
    }
    
    @ResponseBody // view가 아닌 data리턴
    @RequestMapping(value="/weather/selMdl", method=RequestMethod.POST, produces="text/plain;charset=utf-8")
    public String selMdl(@RequestParam("selMdl")String selMdl) throws Exception {
        logger.info("selMdl : "+selMdl);
        CoordFetcher coord = new CoordFetcher();
        JSONArray mapleaf = coord.getMapLeaf(top, selMdl);
        //System.out.println(mapleaf.toJSONString());
        mdl=selMdl;
        return mapleaf.toJSONString();
    }
	
    @ResponseBody // view가 아닌 data리턴
    @RequestMapping(value="/weather/selLeaf", method=RequestMethod.POST, produces="text/plain;charset=utf-8")
    public String selLeaf(@RequestParam("selLeaf")String selLeaf) throws Exception {
        logger.info("selLeaf : "+selLeaf);
        leaf = selLeaf;
        CoordFetcher coord = new CoordFetcher();
        ApiExplorerWeather api = new ApiExplorerWeather(coord.fetchCoord(top,mdl,leaf));
    	JsonArray js = api.getJArray();
        return js.toString();
    }
    
    @RequestMapping(value="/style", method=RequestMethod.POST, produces="text/plain;charset=utf-8")
    public String style(@ModelAttribute StyleVO vo, Model model, @RequestParam("tmn")String tmn, @RequestParam("tmx")String tmx) throws Exception {
		logger.info("style");
		
		System.out.println("tmn : "+tmn);
		System.out.println("tmx : "+tmx);
		
		int avgTemp = (Integer.parseInt(tmn) + Integer.parseInt(tmx)) / 2;
		String[] data = new String[4];

		if(avgTemp <= 4)
		{
			//패딩, 두꺼운코트
			data[0] = "padding";
			data[1] = "coat";
		}
		else if(avgTemp >= 5 && avgTemp <= 8)
		{
			//코트, 가죽자켓
			data[0] = "coat";
			data[1] = "leather-jacket";
		}
		else if(avgTemp >= 9 && avgTemp <= 11)
		{
			//자켓, 트렌치코트
			data[0] = "coat";
			data[1] = "jacket";
		}
		else if(avgTemp >= 12 && avgTemp <= 16)
		{
			//자켓, 가디건
			data[0] = "jacket";
		}
		else if(avgTemp >= 17 && avgTemp <= 19)
		{
			//자켓, 가디건, 티셔츠
			data[0] = "jacket";
			data[1] = "t-shirt";
		}
		else if(avgTemp >= 20 && avgTemp <= 22)
		{
			//가디건, 티셔츠
			data[0] = "t-shirt";
		}
		else if(avgTemp >= 23 && avgTemp <= 27)
		{
			//반팔, 티셔츠, 셔츠, 반팔셔츠
			data[0] = "t-shirt";
			data[1] = "harf-tshirt";
			data[2] = "shirt";
			data[3] = "harf-shirt";			
		}
		else if(avgTemp >= 28)
		{
			//반팔, 민소매
			data[0] = "harf-tshirt";
			data[1] = "harf-shirt";
		}
		
//		HashMap<String, String[]> hm = new HashMap<String, String[]>();
//		hm.put("data", data) ;
//		
//		List<StyleVO> StyleList = styleDAO.StyleList(hm);
//		model.addAttribute("StyleList", StyleList);

		
		
		return "coordination/imageView";
	}
    
    //고객의 소리 - Java Mail API
    @RequestMapping(value = "mail", method = RequestMethod.POST)
    public String mail(HttpServletRequest request, HttpSession memberSession)throws AddressException, MessagingException {

    	String host = "smtp.naver.com";
    	
    	//보내는 사람 정보
    	final String user = "sunrns126@naver.com";
    	final String password = "";
    	
    	// SMTP 서버 정보를 설정
    	Properties props = new Properties();
    	props.put("mail.smtp.host", host);
    	props.put("mail.smtp.port", 587);
    	props.put("mail.smtp.auth", "true");
    	
    	Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
    		protected PasswordAuthentication getPasswordAuthentication() {
    			return new PasswordAuthentication(user, password); 
    		} 
    	});
    	
    	try {
    		
    		MimeMessage message = new MimeMessage(session);
    		message.setFrom(new InternetAddress(user));
    		
    		//받는 사람 정보
    		message.addRecipient(Message.RecipientType.TO, new InternetAddress("sangwon7482@naver.com"));
    		
    		//메일 제목
    		String subject = memberSession.getAttribute("userName").toString() + "님의 건의사항 입니다.";
    		message.setSubject(subject);
    		//메일 내용
    		String body = request.getParameter("mail");
    		message.setText(body);
    		
    		// send the message 
    		Transport.send(message);
    		System.out.println("Success Message Send"); 
    		
    	}catch(MessagingException e) {
    		e.printStackTrace(); 
    	}

    	return "redirect:/";
    }
}

