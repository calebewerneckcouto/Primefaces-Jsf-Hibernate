package managedBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoTelefone;
import dao.DaoUsuario;
import model.TelefoneUser;
import model.UsuarioPessoa;


@ManagedBean(name="telfoneManagedBean")
@ViewScoped
public class TelefoneManagedBean {
	
	
	private UsuarioPessoa user = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoUser = new DaoUsuario<UsuarioPessoa>();
	private DaoTelefone<TelefoneUser> daoTelefone = new DaoTelefone<TelefoneUser>();
	
	private TelefoneUser telefone = new TelefoneUser();
	

	 @PostConstruct
	public void init(){
		
		String coduser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigouser");
		user = daoUser.pesquisar(Long.parseLong(coduser) ,UsuarioPessoa.class);
		System.out.println(coduser);
	}
	 
	 
	 public String salvar(){
		 telefone.setUsuarioPessoa(user);
		 daoTelefone.salvar(telefone);
		 telefone = new TelefoneUser();
		 user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class);
		 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Informação:","Telefone salvo com sucesso!"));
		 
		 return "";
	 }
	 
	 public String removeTelefone() throws Exception{
		 
		 daoTelefone.deletarPoId(telefone);
		 user = daoUser.pesquisar(user.getId() ,UsuarioPessoa.class);
		 telefone = new TelefoneUser();
		 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Informação:","Telefone removido com sucesso!"));
		 
		 return"";
	 }
	 
	 
	 
	 
	 public void setTelefone(TelefoneUser telefone) {
		this.telefone = telefone;
	}
	 
	 public TelefoneUser getTelefone() {
		return telefone;
	}
	 
	
	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}
	
	public UsuarioPessoa getUser() {
		return user;
	}
	public void setDaoTelefone(DaoTelefone<TelefoneUser> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}
	public DaoTelefone<TelefoneUser> getDaoTelefone() {
		return daoTelefone;
	}
	
}
