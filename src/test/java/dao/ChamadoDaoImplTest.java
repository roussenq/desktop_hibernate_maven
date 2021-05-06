/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidade.Chamado;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import static org.junit.Assert.*;
import static br.com.utilitario.UtilGerador.*;
import util.GeradorTabela;

/**
 *
 * @author User
 */
public class ChamadoDaoImplTest {

    private Chamado chamado;
    private ChamadoDao chamadoDao;
    private Session sessao;

    public ChamadoDaoImplTest() {
        chamadoDao = new ChamadoDaoImpl();
    }

    @Test
    public void testSalvarChamado() {
        System.out.println("Salvar chamado");

        chamado = new Chamado(  
                "PC" + gerarNumInteiro(3),
                gerarDescricaoChamadoTi()
        );

        sessao = HibernateUtil.abrirConexao();
        chamadoDao.salvarOuAlterar(chamado, sessao);
        sessao.close();

        assertNotNull(chamado.getId());
    }

    //@Test
    public void testAlterarChamado() {
        System.out.println("Alterar chamado");
        buscarChamadoBd();
        chamado.setAtivo(false);
        sessao = HibernateUtil.abrirConexao();
        chamadoDao.salvarOuAlterar(chamado, sessao);
        sessao.close();

        sessao = HibernateUtil.abrirConexao();
        Chamado chamadoAlterado = chamadoDao.pesquisarPorId(chamado.getId(), sessao);
        sessao.close();

        assertEquals(chamado.isAtivo(), chamadoAlterado.isAtivo());
    }

    //@Test
    public void testExcluirChamado() {
        System.out.println("Excluir chamado");
        buscarChamadoBd();
        sessao = HibernateUtil.abrirConexao();
        chamadoDao.excluir(chamado, sessao);
        Chamado chamadoExc = chamadoDao.pesquisarPorId(chamado.getId(), sessao);
        sessao.close();
        assertNull(chamadoExc);
    }

    //@Test
    public void testPesquisarPorEquipamento() {
        System.out.println("pesquisar Por Equipamento");
        buscarChamadoBd();
        sessao = HibernateUtil.abrirConexao();
        List<Chamado> chamados = chamadoDao.pesquisarPorEquipamento(chamado.getEquipamento().substring(0, 3), sessao);
        sessao.close();
        assertTrue(!chamados.isEmpty());
    }

    //@Test
    public void testPesquisarChamadoAberto() {
        System.out.println("pesquisarChamadoAberto");
        buscarChamadoBd();
        sessao = HibernateUtil.abrirConexao();
        List<Chamado> chamados = chamadoDao.pesquisarChamadoAberto(sessao);
        sessao.close();
        assertTrue(!chamados.isEmpty());
    }

    public Chamado buscarChamadoBd() {

        sessao = HibernateUtil.abrirConexao();
        Query consulta = sessao.createQuery("from Chamado");
        List<Chamado> chamados = consulta.list();
        sessao.close();

        if (chamados.isEmpty()) {
            testSalvarChamado();
        } else {
            chamado = chamados.get(0);
        }
        return chamado;
    }

}
