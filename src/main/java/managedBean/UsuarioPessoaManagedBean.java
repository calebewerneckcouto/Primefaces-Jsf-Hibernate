package managedBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmail;
import dao.DaoGeneric;
import dao.DaoUsuario;
import model.EmailUser;
import model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped
// carrega os dados na tela
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	private BarChartModel barCharModel = new BarChartModel();
	private EmailUser emailUser = new EmailUser();
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>();
	private String campoPesquisa;

	@PostConstruct
	public void init() {
		list = daoGeneric.listar(UsuarioPessoa.class);
		montarGrafico();
	}

	private void montarGrafico() {
		barCharModel = new BarChartModel();
		ChartSeries userSalario = new ChartSeries("SalÃ¡rio dos Usuarios");
		userSalario.setLabel("Users");
		for (UsuarioPessoa usuarioPessoa : list) {

			userSalario
					.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());

		}
		barCharModel.addSeries(userSalario);
		barCharModel.setTitle("Gráfico de Salários");
	}

	public BarChartModel getBarCharModel() {
		return barCharModel;
	}

	public void pesquisaCep(AjaxBehaviorEvent event) {

		try {
			System.out.println("Cep digitado :" + usuarioPessoa.getCep());

			URL url = new URL("https://viacep.com.br/ws/"
					+ usuarioPessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();

			while ((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}

			UsuarioPessoa userCepPessoa = new Gson().fromJson(
					jsonCep.toString(), UsuarioPessoa.class);

			usuarioPessoa.setCep(userCepPessoa.getCep());
			usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
			usuarioPessoa.setComplemento(userCepPessoa.getComplemento());
			usuarioPessoa.setBairro(userCepPessoa.getBairro());
			usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
			usuarioPessoa.setUf(userCepPessoa.getUf());
			usuarioPessoa.setIbge(userCepPessoa.getIbge());
			usuarioPessoa.setGia(userCepPessoa.getGia());

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public UsuarioPessoa getUsuarioPessoa() {

		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

	public String salvar() {

		daoGeneric.salvar(usuarioPessoa);

		list.add(usuarioPessoa);

		init();

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informaçãoo:",
						"Salvo com sucesso!"));

		return "";
	}

	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}

	public List<UsuarioPessoa> getList() {

		return list;
	}

	public String remover() {

		try {

			daoGeneric.removerUsuario(usuarioPessoa);
			list.remove(usuarioPessoa);

			usuarioPessoa = new UsuarioPessoa();
			init();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Informação:", "Removido com sucesso!"));

		} catch (Exception e) {

			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Informação:",
								"Existem Telefones para o Usuario"));
			} else {
				e.printStackTrace();
			}

		}
		return "";
	}

	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}

	public EmailUser getEmailUser() {
		return emailUser;
	}

	public void addEmail() {
		emailUser.setUsuarioPessoa(usuarioPessoa);
		emailUser = daoEmail.updateMerge(emailUser);
		usuarioPessoa.getEmails().add(emailUser);
		emailUser = new EmailUser();
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informaçãoo:",
						"Email salvo com sucesso!"));
		
	}

	public void removerEmail() throws Exception {

		String codigoemail = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap()
				.get("codigoemail");

		EmailUser remover = new EmailUser();
		remover.setId(Long.parseLong(codigoemail));
		daoEmail.deletarPoId(remover);
		usuarioPessoa.getEmails().remove(remover);
		
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informaçãoo:",
						"Email removido  com sucesso!"));

		
	}

	public void pesquisar() {

		list = daoGeneric.pesquisar(campoPesquisa);
		montarGrafico();
	}

	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}

	public String getCampoPesquisa() {
		return campoPesquisa;
	}

	public void upload(FileUploadEvent image) {

		String imagem = "data:imagem/png;base64,"
				+ DatatypeConverter.printBase64Binary(image.getFile()
						.getContents());
		usuarioPessoa.setImagem(imagem);
	}

	public void donwload() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();

		
		String fileDonwloadId = params.get("fileDonwloadId");
		UsuarioPessoa pessoa = daoGeneric.pesquisar(Long.parseLong(fileDonwloadId),UsuarioPessoa.class);
		byte[] imagem = new Base64().decodeBase64(pessoa.getImagem().split("\\,")[1]);
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	
	   response.addHeader("Content-Disposition", "attachment; filename=donwload.png");
	   response.setContentType("application/octet-stream");
	   response.setContentLength(imagem.length);
	   response.getOutputStream().write(imagem);
	   response.getOutputStream().flush();
	   FacesContext.getCurrentInstance().responseComplete();
	}
}
