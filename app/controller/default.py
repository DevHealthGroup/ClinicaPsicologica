import requests
from app import app, db
from datetime import *
from flask import Flask, url_for, redirect, render_template, request, session
from app.model.tables import Paciente, Consulta, Pagamento
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

    # PEGA AS INFORMAÇÕES DE TODOS OS PACIENTEs 
    pacientes = Paciente.query.all()

    # QUANDO RECEBE OS DADOS DO FORMULÁRIO DE CADASTRO
    if (request.method == 'POST'):

        for p in pacientes:
            if p.doc == int(request.form['doc']):
                return render_template('cadastro.html', erro = "Os dados inseridos já estão cadastrados")
            if p.email == request.form['email']:
                return render_template('cadastro.html', erro = "Os dados inseridos já estão cadastrados")
            if p.telefone == request.form['telefone']:
                return render_template('cadastro.html', erro = "Os dados inseridos já estão cadastrados")

        # ACESSA A API VIACEP UTILIZANDO O CEP PASSADO NO CADASTRO
        urlViaCep = f"https://viacep.com.br/ws/{request.form['cep']}/json/"

        try: 
            requests.get(urlViaCep).json()
        except:
            return render_template('cadastro.html', erro = "O CEP inserido é inválido")

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
                                request.form['tipoPlano'],
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





@app.route("/cadastro/arquivar/<int:id>", methods=['GET', 'POST'])
def arquivar(id):

    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('index'))

    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')

    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(idPaciente)

    pacientes = Paciente.query.all()

    paciente = Paciente.query.get(id)
    paciente.arquivado = 1

    db.session.commit()

    return redirect(url_for("editarcadastro", paciente = pacienteLog, pacientes = pacientes))





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

    # PEGA OS IDs DAS CONSULTAS NO SISTEMAS E ARMAZENA EM UMA LISTA
    pagamentosLog = Pagamento.query.all()
    pagamentos = []
    for pag in pagamentosLog:
        pagamentos.append(pag.consulta)

    # QUANDO RECEBE OS DADOS DO FORMULÁRIO DE CADASTRO
    if (request.method == 'POST'):

        # PEGA O HORARIO PASSADO PELO FORMULÁRIO
        horaAgendada = request.form['hora']

        # PEGA A DATA PASSADA PELO FORMULÁRIO, SEPARA E CONVERTE EM OBJETO DATA
        dataAgendada = request.form['dia']
        dataAgendada = dataAgendada.split('-')
        dataAgendadaBanco = f'{int(dataAgendada[2])} - {int(dataAgendada[1])} - {int(dataAgendada[0])}'
        dataAgendada = date(int(dataAgendada[0]), int(dataAgendada[1]), int(dataAgendada[2]))

        # PEGA A DATA ATUAL DO USUÁRIO
        dataAtual = date.today()

        # 
        if (dataAgendada > (dataAtual + timedelta(days=90))):
            return render_template("consultas.html", paciente = pacienteLog, consultas = consultas, pagamentos = pagamentos, mensagem = "Ops, a data inserida é muito distante")

        # VERIFICA O DIA DA SEMANA
        if ((dataAgendada.weekday()) == 5 or (dataAgendada.weekday()) == 6):
            return render_template("consultas.html", paciente = pacienteLog, consultas = consultas, pagamentos = pagamentos, mensagem = "Ops, não é possível agendar no final de semana")

        # VERIFICA SE A DATA PASSADA PELO FORMULÁRIO É VÁLIDA
        if (dataAgendada < dataAtual or dataAgendada == dataAtual):
            return render_template("consultas.html", paciente = pacienteLog, consultas = consultas, pagamentos = pagamentos, mensagem = "Ops, a data inserida é inválida")

        # VERIFICA SE A DATA PASSADO PELO FORMULÁRIO ESTÁ LIVRE
        for consulta in consultas:
            if ((str(dataAgendada) == consulta.data) and (horaAgendada == consulta.horario)):
                return render_template("consultas.html", paciente = pacienteLog, consultas = consultas, pagamentos = pagamentos, mensagem = "Ops, a data inserida já está marcada")

        # CRIA UM NOVO AGENDAMENTO
        novoAgendamento = Consulta(pacienteLog.idPaciente,
                                   horaAgendada,
                                   dataAgendadaBanco)

        # ADICIONA O NOVO AGENDAMENTO AO BANCO DE DADOS
        db.session.add(novoAgendamento)
        db.session.commit()

        # REDIRECIONA PARA A ROTA DE LOGIN
        return redirect(url_for("consultas", paciente = pacienteLog, consultas = consultas, pagamentos = pagamentos, mensagem = "Agendamento realizado com sucesso!"))


    # RENDERIZA A TELA DE EDIÇÃO DE CADASTRO
    return render_template("consultas.html", paciente = pacienteLog, consultas = consultas, pagamentos = pagamentos)





# ROTA DE APROVAR AGENDAMENTO
@app.route("/consultas/aprovar/<int:id>", methods=['GET', 'POST'])
def aprovarAgendamento(id):

    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('index'))

    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')

    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(idPaciente)

    consulta = Consulta.query.get(id)
    consulta.status = "Agendada"

    db.session.commit()

    return redirect(url_for("consultas", paciente = pacienteLog))





# ROTA DE APROVAR AGENDAMENTO
@app.route("/consultas/reprovar/<int:id>", methods=['GET', 'POST'])
def reprovarAgendamento(id):

    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('index'))

    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')

    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(idPaciente)

    consulta = Consulta.query.get(id)
    consulta.status = "Cancelada"

    db.session.commit()

    return redirect(url_for("consultas", paciente = pacienteLog))





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
    pacientes = Paciente.query.all()
    consultas = Consulta.query.all()

    return render_template("pagamentos.html", paciente = pacienteLog, pagamentos = pagamentos, pacientes = pacientes, consultas = consultas)




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

    # QUANDO RECEBE OS DADOS DO FORMULÁRIO DE CADASTRO
    if (request.method == 'POST'):

        valor = int(request.form['valor'])

        consulta = Consulta.query.get(id)
        paciente = consulta.paciente
        paci = Paciente.query.get(paciente)

        novoPagamento = Pagamento(paci.idPaciente, id, valor, paci.tipoPlano)

        db.session.add(novoPagamento)
        db.session.commit()

        return redirect(url_for("consultas", paciente = pacienteLog))

    return render_template("inserirvalor.html", id = id)





# ROTA PARA DAR BAIXA NO PAGAMENTO
@app.route("/pagar/<int:id>", methods=['GET', 'POST'])
def pagar(id):

    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('index'))

    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')

    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(idPaciente)

    pacientes = Paciente.query.all()
    pagamentos = Pagamento.query.all()
    consultas = Consulta.query.all()

    pagamento = Pagamento.query.get(id)
    pagamento.status = "Pago"

    db.session.commit()

    return redirect(url_for("pagamentos", paciente = pacienteLog, pagamentos = pagamentos, pacientes = pacientes, consultas = consultas))