package posjavamavenhibernate;

import java.util.List;

import model.TelefoneUser;
import model.UsuarioPessoa;

import org.junit.Test;

import dao.DaoGeneric;

public class TesteHibernate {

	@Test
	public void testeHibernateUtil() {// salva os dados no banco

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = new UsuarioPessoa();

		pessoa.setIdade(10);
		pessoa.setLogin("cblkdsvbj");
		pessoa.setNome("fdbbd");
		pessoa.setSobrenome("bdfba");
		pessoa.setSenha("321");

		daoGeneric.salvar(pessoa);

	}

	@Test
	public void testeBuscar2() {

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = daoGeneric.pesquisar(3L, UsuarioPessoa.class);

		System.out.println(pessoa);

	}

	@Test
	public void testeUpdate() {// atualiza os dados no banco de dados

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = daoGeneric.pesquisar(2L, UsuarioPessoa.class);

		pessoa.setIdade(99);
		pessoa.setNome("Nome atualizado");
		pessoa.setSenha("calebe");

		pessoa = daoGeneric.updateMerge(pessoa);

		System.out.println(pessoa);

	}

	@Test
	public void testeDelete() throws Exception {

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = daoGeneric.pesquisar(6L, UsuarioPessoa.class);

		daoGeneric.deletarPoId(pessoa);

	}

	@Test
	public void testeConsultar() {// faz a consulta no banco e tras todos os
									// dados

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		List<UsuarioPessoa> list = daoGeneric.listar(UsuarioPessoa.class);

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			System.out
					.println("------------------------------------------------");
		}

	}

	@Test
	public void testeQueryListMaxResult() {// faz a pesquisa no numero que eu
											// determinar por id ou outro
											// criterio
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery(" from UsuarioPessoa order by id")
				.setMaxResults(2).getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {

			System.out.println(usuarioPessoa);

		}

	}

	@Test
	public void testeQueryListParameter() {// procura um nome ou dado especifico
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		List<UsuarioPessoa> list = daoGeneric
				.getEntityManager()
				.createQuery(
						"from UsuarioPessoa where nome = :nome or sobrenome = :sobrenome")
				.setParameter("nome", "Livia")
				.setParameter("sobrenome", "Couto").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}

	@Test
	public void testeQuerySomaIdade() {// faz a soma das idades dentro do banco
										// de dados e retorna o resultado
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		Long somaIdade = (Long) daoGeneric.getEntityManager()
				.createQuery("select sum(u.idade) from UsuarioPessoa u ")
				.getSingleResult();

		System.out.println("Soma de todas as idades= " + somaIdade);

	}

	@Test
	public void testeNameQuery1() {// ao inves de usar o parametro createQuery
									// ,ele puxa um query criado dentro do
									// Usuario ja estabelecido
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createNamedQuery("UsuarioPessoa.todos").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	@Test
	public void testeNameQuery2() {// ao inves de usar o parametro createQuery
									// ,ele puxa um query criado dentro do
									// Usuario ja estabelecido
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createNamedQuery("UsuarioPessoa.buscaPorNome")
				.setParameter("nome", "fdbbd").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	@Test
	public void testeGravaTelefone() {// salva o telefone para o usuário
										// escolhido

		DaoGeneric daoGeneric = new DaoGeneric();

		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(5L,
				UsuarioPessoa.class);

		TelefoneUser telefoneUser = new TelefoneUser();
		telefoneUser.setTipo("Casa");
		telefoneUser.setNumero("3138218756");
		telefoneUser.setUsuarioPessoa(pessoa);

		daoGeneric.salvar(telefoneUser);

	}

	@Test
	public void testeConsultaTelefones() {// Busca o telefone no banco
		DaoGeneric daoGeneric = new DaoGeneric();

		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(4L,
				UsuarioPessoa.class);

		for (TelefoneUser fone : pessoa.getTelefoneUsers()) {

			System.out.println("Número do telefone: " + fone.getNumero());
			System.out.println("Tipo de Telefone: " + fone.getTipo());
			System.out.println("Nome da Pessoa: "+ fone.getUsuarioPessoa().getNome());
			System.out.println("---------------------------------------------------");

		}

	}
}
