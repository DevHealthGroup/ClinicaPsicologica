import requests
from app import app, db
from datetime import date, datetime
from flask import Flask, url_for, redirect, render_template, request, session
from app.model.tables import Paciente, Consulta, Pagamento, TipoPlano
import json





# ROTA DE LOGIN
@app.route("/", methods=['GET', 'POST'])
def index():

    # LIMPA OS COOKIES
    session.clear()

    # QUANDO RECEBE OS DADOS DO FORMULÁRIO DE LOGIN
    if (request.method == 'POST'):
        pacientes = Paciente.query.all()
        email = request.form['email']
        senha = request.form['senha']

        # PERCORRE TODOS OS PACIENTES NA TABELA PACIENTE NO BANCO DE DADOS
        for paciente in pacientes:
            if (paciente.email == email) and (paciente.senha == senha):
                # REDIRECIONA PARA A ROTA DA HOMEPAGE E PASSA O ID DO PACIENTE
                session['login'] = paciente.idPaciente
                return redirect(url_for('homepage', id = paciente.idPaciente))


        # RENDERIZA A TELA DE LOGIN COM A MENSAGEM DE ERRO
        return render_template("login.html", erro = "Email ou senha incorretos")


    # RENDERIZA A TELA DE LOGIN
    return render_template("login.html")





# ROTA DE CADASTRO
@app.route("/cadastro", methods=['GET', 'POST'])
def cadastro():

    # QUANDO RECEBE OS DADOS DO FORMULÁRIO DE CADASTRO
    if (request.method == 'POST'):

        # ACESSA A API VIACEP UTILIZANDO O CEP PASSADO NO CADASTRO
        urlViaCep = f"https://viacep.com.br/ws/{request.form['cep']}/json/"

        logradouro = requests.get(urlViaCep).json()['logradouro']
        complemento = request.form['complemento']
        cep = request.form['cep']
        bairro = requests.get(urlViaCep).json()['bairro']
        localidade = requests.get(urlViaCep).json()['localidade']
        uf = requests.get(urlViaCep).json()['uf']
        endereco_completo =  f"{logradouro}, {complemento}, {cep}, {bairro}, {localidade}, {uf}"

        # CRIA UM NOVO PACIENTE, PASSANDO TODOS OS DADOS DO FORMULÁRIO + ENDEREÇO COMPLETO COMO ARGUMENTO
        novoPaciente = Paciente(request.form['nome'], 
                                request.form['doc'], 
                                request.form['sexo'], 
                                request.form['nasc'], 
                                endereco_completo, 
                                request.form['telefone'], 
                                request.form['email'], 
                                request.form['senha'])

        # ADICIONA O NOVO PACIENTE AO BANCO DE DADOS
        db.session.add(novoPaciente)
        db.session.commit()

        # REDIRECIONA PARA A ROTA DE LOGIN
        return redirect(url_for('index'))

    # RENDERIZA A TELA DE CADASTRO
    return render_template("cadastro.html")





# ROTA DE EDITAR CADASTRO
@app.route("/cadastro/editar", methods=['GET', 'POST'])
def editarcadastro():

    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('index'))

    # PASSA O ID DO USUÁRIO
    id = session.get('login')

    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(id)

    # PEGA AS INFORMAÇÕES DE TODOS OS PACIENTEs 
    pacientes = Paciente.query.all()

    # SEPARA A STRING DO ENDEREÇO
    enderecoPaciente = pacienteLog.endereco.split(", ")

    # QUANDO RECEBE OS DADOS DO FORMULÁRIO
    if request.method == 'POST':

        # ACESSA A API VIACEP UTILIZANDO O CEP PASSADO NO FORMULÁRIO
        urlViaCep = f"https://viacep.com.br/ws/{request.form['cep']}/json/"

        logradouro = requests.get(urlViaCep).json()['logradouro']
        complemento = request.form['complemento']
        cep = request.form['cep']
        bairro = requests.get(urlViaCep).json()['bairro']
        localidade = requests.get(urlViaCep).json()['localidade']
        uf = requests.get(urlViaCep).json()['uf']
        endereco_completo =  f"{logradouro}, {complemento}, {cep}, {bairro}, {localidade}, {uf}"

        pacienteLog.nome = request.form['nome']
        pacienteLog.sexo = request.form['sexo']
        pacienteLog.endereco = endereco_completo
        pacienteLog.telefone = request.form['telefone']
        pacienteLog.email = request.form['email']
        pacienteLog.senha = request.form['senha']

        db.session.commit()

        # RENDERIZA A TELA DE EDIÇÃO DE CADASTRO
        return redirect(url_for("editarcadastro", id = pacienteLog.idPaciente))

    # RENDERIZA A TELA DE EDIÇÃO DE CADASTRO
    return render_template("editcadastro.html", paciente = pacienteLog, endereco = enderecoPaciente, pacientes = pacientes)





# ROTA DA HOMEPAGE
@app.route("/homepage", methods=['GET', 'POST'])
def homepage():

    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('index'))

    # PASSA O ID DO USUÁRIO
    id = session.get('login')

    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(id)

    # RENDERIZA A TELA DA HOMEPAGE PASSANDO O PACIENTE
    return render_template("homepage.html", paciente = pacienteLog)





# ROTA DE CONSULTAS
@app.route("/consultas", methods=['GET', 'POST'])
def consultas():

    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('index'))

    # PASSA O ID DO USUÁRIO
    id = session.get('login')

    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(id)

    # PEGA AS CONSULTAS
    consultas = Consulta.query.all()

    # QUANDO RECEBE OS DADOS DO FORMULÁRIO DE CADASTRO
    if (request.method == 'POST'):

        # PEGA O HORARIO PASSADO PELO FORMULÁRIO
        horaAgendada = request.form['hora']

        # PEGA A DATA PASSADA PELO FORMULÁRIO, SEPARA E CONVERTE EM OBJETO DATA
        dataAgendada = request.form['dia']
        dataAgendada = dataAgendada.split('-')
        dataAgendada = date(int(dataAgendada[0]), int(dataAgendada[1]), int(dataAgendada[2]))

        # PEGA A DATA ATUAL DO USUÁRIO
        dataAtual = date.today()

        # VERIFICA SE A DATA PASSADA PELO FORMULÁRIO É VÁLIDA
        if (dataAgendada < dataAtual or dataAgendada == dataAtual):
            return render_template("consultas.html", paciente = pacienteLog, consultas = consultas, mensagem = "Ops, a data inserida é inválida")

        # VERIFICA SE A DATA PASSADO PELO FORMULÁRIO ESTÁ LIVRE
        for consulta in consultas:
            if ((str(dataAgendada) == consulta.data) and (horaAgendada == consulta.horario)):
                return render_template("consultas.html", paciente = pacienteLog, consultas = consultas, mensagem = "Ops, a data inserida já está marcada")

        # CRIA UM NOVO AGENDAMENTO
        novoAgendamento = Consulta(pacienteLog.idPaciente,
                                   horaAgendada,
                                   dataAgendada)

        # ADICIONA O NOVO AGENDAMENTO AO BANCO DE DADOS
        db.session.add(novoAgendamento)
        db.session.commit()

        # REDIRECIONA PARA A ROTA DE LOGIN
        return redirect(url_for("consultas", paciente = pacienteLog, consultas = consultas, mensagem = "Agendamento realizado com sucesso!"))


    # RENDERIZA A TELA DE EDIÇÃO DE CADASTRO
    return render_template("consultas.html", paciente = pacienteLog, consultas = consultas)





# ROTA DE PAGAMENTOS
@app.route("/pagamentos", methods=["GET", "POST"])
def pagamentos():

    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('index'))

    # PASSA O ID DO USUÁRIO
    id = session.get('login')

    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(id)

    # PEGA OS PAGAMENTOS DO BANCO DE DADOS
    pagamentos = Pagamento.query.all()

    return render_template("pagamentos.html", paciente = pacienteLog, pagamentos = pagamentos)




# ROTA GERAR PAGAMENTO
@app.route("/gerar-pagamento/<int:id>", methods=['GET', 'POST'])
def gerarPagamento(id):

    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('index'))

    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')

    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(idPaciente)

    consulta = Consulta.query.get(id)
    paciente = consulta.paciente
    paci = Paciente.query.get(paciente)

    novoPagamento = Pagamento(id, paci.idPaciente, 50.00, paci.tipoPlano)

    db.session.add(novoPagamento)
    db.session.commit()

    return redirect(url_for("consultas", paciente = pacienteLog))