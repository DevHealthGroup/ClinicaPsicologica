import requests
from app import app, db
from datetime import *
from flask import Flask, url_for, redirect, render_template, request, session, jsonify
from app.model.tables import Paciente, Consulta, Pagamento
import json

# RODA INDEX
@app.route("/", methods=['GET', 'POST'])
def index():
    return render_template("index.html")

# RODA ARTIGOS
@app.route("/artigos/<int:id>", methods=['GET', 'POST'])
def artigos(id):
    if id == 0:
        return render_template("artigos.html")
    if id == 1:
        return render_template("artigo-1.html")
    if id == 2:
        return render_template("artigo-2.html")
    if id == 3:
        return render_template("artigo-3.html")
    if id == 4:
        return render_template("artigo-4.html")
    if id == 5:
        return render_template("artigo-5.html")

# RODA CONTATO
@app.route("/contato", methods=['GET', 'POST'])
def contato():
    return render_template("contato.html")

# ROTA DE LOGIN
@app.route("/login", methods=['GET', 'POST'])
def login():
    # LIMPA OS COOKIES
    session.clear()
    # QUANDO RECEBE OS DADOS
    if (request.method == 'POST'):
        # BUSCA TODOS OS PACIENTES DO BANCO E OS DADOS PASSADOS PELO USUÁRIO
        pacientes = Paciente.query.all()
        email = request.form['email']
        senha = request.form['senha']
        # PERCORRE TODOS OS PACIENTES NA TABELA PACIENTE NO BANCO DE DADOS
        for paciente in pacientes:
            # SE OS DADOS PASSADOS PELO USUÁRIO CORRESPONDEREM COM ALGUM DO BANCO DE DADOS
            if (paciente.email == email) and (paciente.senha == senha):
                # REDIRECIONA PARA A ROTA DA HOMEPAGE PASSANDO O ID E ADICIONA O ID NOS COOKIES
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
        # VERIFICA SE OS DADOS INSERIDOS NÃO SÃO CADASTRADOS NO SISTEMA
        for p in pacientes:
            if p.doc == int(request.form['doc']):
                return render_template('cadastro.html', erro = "Os dados inseridos já estão cadastrados")
            if p.email == request.form['email']:
                return render_template('cadastro.html', erro = "Os dados inseridos já estão cadastrados")
            if p.telefone == request.form['telefone']:
                return render_template('cadastro.html', erro = "Os dados inseridos já estão cadastrados")
        # ACESSA A API VIACEP UTILIZANDO O CEP PASSADO NO CADASTRO
        urlViaCep = f"https://viacep.com.br/ws/{request.form['cep']}/json/"
        # VERIFICA SE O CEP INSERIDO É VÁLIDO
        try:
            requests.get(urlViaCep).json()
        except:
            return render_template('cadastro.html', erro = "O CEP inserido é inválido")
        # MONTA O ENDEREÇO COMPLETO DO PACIENTE
        logradouro = requests.get(urlViaCep).json()['logradouro']
        numero = request.form['numero']
        complemento = request.form['complemento']
        cep = request.form['cep']
        bairro = requests.get(urlViaCep).json()['bairro']
        localidade = requests.get(urlViaCep).json()['localidade']
        uf = requests.get(urlViaCep).json()['uf']
        endereco_completo =  f"{logradouro}, {numero}, {complemento}, {cep}, {bairro}, {localidade}, {uf}"
        # CRIA UM NOVO PACIENTE, PASSANDO TODOS OS DADOS DO FORMULÁRIO + ENDEREÇO COMPLETO COMO ARGUMENTO
        novoPaciente = Paciente(request.form['nome'],
                                request.form['doc'],
                                request.form['sexo'],
                                request.form['nasc'],
                                endereco_completo,
                                request.form['telefone'],
                                request.form['email'],
                                request.form['convenio'],
                                request.form['senha'])
        # ADICIONA O NOVO PACIENTE AO BANCO DE DADOS
        db.session.add(novoPaciente)
        db.session.commit()
        # REDIRECIONA PARA A ROTA DE LOGIN
        return redirect(url_for('login'))
    # RENDERIZA A TELA DE CADASTRO
    return render_template("cadastro.html")

# ROTA DE EDITAR CADASTRO
@app.route("/cadastro/editar", methods=['GET', 'POST'])
def editarcadastro():
    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('login'))
    # REGISTRA O ID DO USUÁRIO DE ACORDO COM OS COOKIES
    id = session.get('login')
    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NA URL
    pacienteLog = Paciente.query.get(id)
    # PEGA AS INFORMAÇÕES DE TODOS OS PACIENTEs
    pacientes = Paciente.query.all()
    # SEPARA A STRING DO ENDEREÇO
    enderecoPaciente = pacienteLog.endereco.split(", ")
    # QUANDO RECEBE OS DADOS
    if request.method == 'POST':
        # ACESSA A API VIACEP UTILIZANDO O CEP PASSADO NO FORMULÁRIO
        urlViaCep = f"https://viacep.com.br/ws/{request.form['cep']}/json/"
        # VERIFICA SE O CEP INSERIDO É VÁLIDO
        try:
            requests.get(urlViaCep).json()
        except:
            return redirect(url_for("editarcadastro", id = pacienteLog.idPaciente))
        # MONTA O ENDEREÇO COMPLETO DO PACIENTE
        logradouro = requests.get(urlViaCep).json()['logradouro']
        numero = request.form['numero']
        complemento = request.form['complemento']
        cep = request.form['cep']
        bairro = requests.get(urlViaCep).json()['bairro']
        localidade = requests.get(urlViaCep).json()['localidade']
        uf = requests.get(urlViaCep).json()['uf']
        endereco_completo =  f"{logradouro}, {numero}, {complemento}, {cep}, {bairro}, {localidade}, {uf}"
        # ALTERA OS DADOS DO PACIENTE
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

# ROTA PARA ARQUIVAR PACIENTE
@app.route("/cadastro/arquivar/<int:id>", methods=['GET', 'POST'])
def arquivar(id):
    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('login'))
    # PASSA O ID DO USUÁRIO E PEGA ELE NO BANCO DE DADOS
    idPaciente = session.get('login')
    pacienteLog = Paciente.query.get(idPaciente)
    # PEGA TODOS OS PACIENTES DO BANCO DE DADOS
    pacientes = Paciente.query.all()
    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    paciente = Paciente.query.get(id)
    # DEFINE O PACIENTE COMO ARQUIVADO
    paciente.arquivado = 1
    db.session.commit()
    # REDIRECIONA PARA A ROTA DOS CADASTROS
    return redirect(url_for("editarcadastro", paciente = pacienteLog, pacientes = pacientes))

# ROTA PARA DESARQUIVAR PACIENTE
@app.route("/cadastro/desarquivar/<int:id>", methods=['GET', 'POST'])
def desarquivar(id):
    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('login'))
    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')
    pacienteLog = Paciente.query.get(idPaciente)
    # PEGA TODOS OS PACIENTES DO BANCO DE DADOS
    pacientes = Paciente.query.all()
    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    paciente = Paciente.query.get(id)
    # DEFINE O PACIENTE COMO DESARQUIVADO
    paciente.arquivado = 0
    db.session.commit()
    # REDIRECIONA PARA A ROTA DOS CADASTROS
    return redirect(url_for("editarcadastro", paciente = pacienteLog, pacientes = pacientes))

# ROTA DA HOMEPAGE
@app.route("/homepage", methods=['GET', 'POST'])
def homepage():
    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('login'))
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
        return redirect(url_for('login'))
    # PASSA O ID DO USUÁRIO
    id = session.get('login')
    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(id)
    pacienteAll = Paciente.query.all()
    # PEGA AS CONSULTAS
    consultas = Consulta.query.all()
    consultas = list(reversed(consultas))
    # PEGA OS IDs DAS CONSULTAS NO SISTEMAS E ARMAZENA EM UMA LISTA
    pagamentosLog = Pagamento.query.all()
    pagamentos = []
    for p in pagamentosLog:
        pagamentos.append(p.consulta)
    # QUANDO RECEBE OS DADOS
    if (request.method == 'POST'):
        # PEGA O HORARIO PASSADO PELO FORMULÁRIO
        horaAgendada = request.form['hora']
        # PEGA A DATA PASSADA PELO FORMULÁRIO, SEPARA E CONVERTE EM OBJETO DATA
        dataAgendada = request.form['dia']
        dataAgendada = dataAgendada.split('-')
        dataAgendadaBanco = f'{int(dataAgendada[2])} / {int(dataAgendada[1])} / {int(dataAgendada[0])}'
        dataAgendada = date(int(dataAgendada[0]), int(dataAgendada[1]), int(dataAgendada[2]))
        # PEGA A DATA ATUAL DO USUÁRIO
        dataAtual = date.today()
        # VERIFICA SE A DATA É MAIOR QUE 3 MESES
        if (dataAgendada > (dataAtual + timedelta(days=90))):
            return render_template("consultas.html", paciente = pacienteLog, pacientes = pacienteAll, consultas = consultas, pagamentos = pagamentos, mensagem = "Ops, a data inserida é muito distante")
        # VERIFICA SE A DATA INFORMA É FINAL DE SEMANA
        if ((dataAgendada.weekday()) == 5 or (dataAgendada.weekday()) == 6):
            return render_template("consultas.html", paciente = pacienteLog, pacientes = pacienteAll, consultas = consultas, pagamentos = pagamentos, mensagem = "Ops, não é possível agendar no final de semana")
        # VERIFICA SE A DATA PASSADA PELO FORMULÁRIO É VÁLIDA
        if (dataAgendada < dataAtual or dataAgendada == dataAtual):
            return render_template("consultas.html", paciente = pacienteLog, pacientes = pacienteAll, consultas = consultas, pagamentos = pagamentos, mensagem = "Ops, a data inserida é inválida")
        # VERIFICA SE A DATA PASSADO PELO FORMULÁRIO ESTÁ LIVRE
        for consulta in consultas:
            if ((str(dataAgendadaBanco) == consulta.data) and (horaAgendada == consulta.horario)):
                return render_template("consultas.html", paciente = pacienteLog, pacientes = pacienteAll, consultas = consultas, pagamentos = pagamentos, mensagem = "Ops, a data inserida já está marcada")
        # CRIA UM NOVO AGENDAMENTO
        novoAgendamento = Consulta(pacienteLog.idPaciente,
                                   horaAgendada,
                                   dataAgendadaBanco)
        # ADICIONA O NOVO AGENDAMENTO AO BANCO DE DADOS
        db.session.add(novoAgendamento)
        db.session.commit()
        # REDIRECIONA PARA A ROTA DE CONSULTAS PASSANDO OS DADOS E A MENSAGEM
        return redirect(url_for("consultas", paciente = pacienteLog, pacientes = pacienteAll, consultas = consultas, pagamentos = pagamentos, mensagem = "Agendamento realizado com sucesso!"))
    # RENDERIZA A TELA DE CONSULTAS
    return render_template("consultas.html", paciente = pacienteLog, pacientes = pacienteAll, consultas = consultas, pagamentos = pagamentos)

# ROTA DE APROVAR AGENDAMENTO
@app.route("/consultas/aprovar/<int:id>", methods=['GET', 'POST'])
def aprovarAgendamento(id):
    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('login'))
    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')
    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(idPaciente)
    # PEGA A CONSULTA PELO ID
    consulta = Consulta.query.get(id)
    # DEFINE COMO AGENDADA
    consulta.status = "Agendada"
    db.session.commit()
    # REDIRECIONA PARA A ROTA DE CONSULTAS
    return redirect(url_for("consultas", paciente = pacienteLog))

# ROTA DE APROVAR AGENDAMENTO
@app.route("/consultas/reprovar/<int:id>", methods=['GET', 'POST'])
def reprovarAgendamento(id):
    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('login'))
    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')
    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(idPaciente)
    # PEGA A CONSULTA PELO ID
    consulta = Consulta.query.get(id)
    # DEFINIE COMO CANCELADA
    consulta.status = "Cancelada"
    db.session.commit()
    # REDIRECIONA PARA A ROTA DE CONSULTAS
    return redirect(url_for("consultas", paciente = pacienteLog))

# ROTA DE PAGAMENTOS
@app.route("/pagamentos", methods=["GET", "POST"])
def pagamentos():
    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('login'))
    # PASSA O ID DO USUÁRIO
    id = session.get('login')
    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(id)
    # PEGA TODOS OS PAGAMENTOS DO BANCO DE DADOS
    pagamentos = Pagamento.query.all()
    # PEGA TODOS OS PACIENTES DO BANCO DE DADOS
    pacientes = Paciente.query.all()
    # PEGA TODAS AS CONSULTAS DO BANCO DE DADOS
    consultas = Consulta.query.all()
    # RENDERIZA A TELA DE PAGAMENTOS PASSANDO TODOS OS DADOS
    return render_template("pagamentos.html", paciente = pacienteLog, pagamentos = pagamentos, pacientes = pacientes, consultas = consultas)

# ROTA GERAR PAGAMENTO
@app.route("/gerar-pagamento/<int:id>", methods=['GET', 'POST'])
def gerarPagamento(id):
    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('login'))
    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')
    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(idPaciente)
    # QUANDO RECEBE OS DADOS DO FORMULÁRIO DE CADASTRO
    if (request.method == 'POST'):
        # VALOR INFORMADO PELA PSICÓLOGA
        valor = request.form['valor']
        valor = valor.split(".")
        isFloat = True
        try:
            valor[1]
        except:
            isFloat = False
        if (isFloat == False):
            valor = str(request.form['valor'])
        else:
            valor = float(request.form['valor'])
            valor = round(valor, 2)
            valor = str(valor)
        # PEGA A CONSULTA PELO ID
        consulta = Consulta.query.get(id)
        # PEGA OS DADOS DO PACIENTE DAQUELA CONSULTA
        paciente = consulta.paciente
        paci = Paciente.query.get(paciente)
        # GERA O NOVO PAGAMENTO NO BANCO DE DADOS
        novoPagamento = Pagamento(paci.idPaciente, id, valor, paci.convenio)
        db.session.add(novoPagamento)
        db.session.commit()
        # REDIRECIONA PARA A ROTA DE CONSULTAS
        return redirect(url_for("consultas", paciente = pacienteLog))
    # RENDERIZA A TELA DE INSERIR O VALOR DA CONSULTA
    return render_template("inserirvalor.html", id = id)

# ROTA PARA DAR BAIXA NO PAGAMENTO
@app.route("/pagar/<int:id>", methods=['GET', 'POST'])
def pagar(id):
    # VERIFICA SE O ID DO USUÁRIO ESTÁ NOS COOKIES
    if ('login' not in session):
        return redirect(url_for('login'))
    # PASSA O ID DO USUÁRIO
    idPaciente = session.get('login')
    # PEGA AS INFORMAÇÕES DO PACIENTE PELO ID PASSADO NO URL
    pacienteLog = Paciente.query.get(idPaciente)
    # PEGA TODOS OS PACIENTES DO BANCO DE DADOS
    pacientes = Paciente.query.all()
    # PEGA TODOS OS PAGAMENTOS DO BANCO DE DADOS
    pagamentos = Pagamento.query.all()
    # PEGA TODAS AS CONSULTAS DO BANCO DE DADOS
    consultas = Consulta.query.all()
    # PEGA O PAGAMENTO PELO ID
    pagamento = Pagamento.query.get(id)
    # DEFINE COMO PAGO
    pagamento.status = "Pago"
    db.session.commit()
    # REDIRECIONA PARA A RODA DE PAGAMENTOS
    return redirect(url_for("pagamentos", paciente = pacienteLog, pagamentos = pagamentos, pacientes = pacientes, consultas = consultas))

# ROTA GET CONSULTAS PARA MOBILE
@app.route("/data/consultas/<int:id>/<string:email>", methods=["GET"])
def getConsultas(id, email):
    # VERIFICA SE A CHAVE
    if id == 3219876547531596482:
        # PEGA TODAS AS CONSULTA E PACIENTES DO BACNO
        allConsultas = []
        consultas = Consulta.query.all()
        pacientes = Paciente.query.all()
        consultaAtual = {}
        for c in consultas:
            if email == "admin@admin.com":
                consultaAtual = {"idConsulta": c.idConsulta,
                                "paciente": c.paciente,
                                "horario": c.horario,
                                "data": c.data,
                                "status": c.status}
                allConsultas.append(consultaAtual)

            for p in pacientes:
                if p.email == email:
                    if p.idPaciente == c.paciente:
                        if c.idConsulta not in allConsultas:
                            consultaAtual = {"idConsulta": c.idConsulta,
                                            "paciente": c.paciente,
                                            "horario": c.horario,
                                            "data": c.data,
                                            "status": c.status}
                            allConsultas.append(consultaAtual)
        allConsultas = list(reversed(allConsultas))
        return jsonify(allConsultas)


# ROTA GET PAGAMENTOS PARA MOBILE
@app.route("/data/pagamentos/<int:id>/<string:email>", methods=["GET"])
def getPagamentos(id, email):
    # VERIFICA A CHAVE
    if id == 98765432135715924687:
        pagamentos = Pagamento.query.all()
        pacientes = Paciente.query.all()
        allPagamentos = []
        pagamentoAtual = {}
        for p in pagamentos:
            if email == "admin@admin.com":
                pagamentoAtual = {"idPagamento": p.idPagamento,
                                "consulta": p.consulta,
                                "paciente": p.paciente,
                                "valor": p.valor,
                                "status": p.status,
                                "convenio": p.convenio}
                allPagamentos.append(pagamentoAtual)
            for pp in pacientes:
                if pp.email == email:
                    if pp.idPaciente == p.paciente:
                        if p.paciente not in allPagamentos:
                            pagamentoAtual = {"idPagamento": p.idPagamento,
                                            "consulta": p.consulta,
                                            "paciente": p.paciente,
                                            "valor": p.valor,
                                            "status": p.status,
                                            "convenio": p.convenio}
                            allPagamentos.append(pagamentoAtual)
        return jsonify(allPagamentos)

# ROTA DE CADASTRO VIA MOBILE
@app.route("/cadastro/api", methods=['POST'])
def cadastro_api():
    # PEGA AS INFORMAÇÕES DE TODOS OS PACIENTEs
    if (request.json['nome'] == "" or request.json['doc'] == "" or request.json['sexo'] == "" or request.json['dtNasc'] == "" or request.json['cep'] == "" or request.json['numero'] == "" or request.json['telefone'] == "" or request.json['email'] == "" or request.json['senha'] == ""):
        return "False"
    pacientes = Paciente.query.all()
    # QUANDO RECEBE OS DADOS
    for p in pacientes:
        if p.doc == int(request.json['doc']):
            return "False"
        if p.email == request.json['email']:
            return "False"
        if p.telefone == request.json['telefone']:
            return "False"
    # ACESSA A API VIACEP UTILIZANDO O CEP PASSADO NO CADASTRO
    urlViaCep = f"https://viacep.com.br/ws/{request.json['cep']}/json/"
    # VERIFICA SE O CEP INSERIDO É VÁLIDO
    try:
        requests.get(urlViaCep).json()
    except:
        return "False"
    # MONTA O ENDEREÇO COMPLETO DO PACIENTE
    logradouro = requests.get(urlViaCep).json()['logradouro']
    numero = request.json['numero']
    complemento = request.json['complemento']
    cep = request.json['cep']
    bairro = requests.get(urlViaCep).json()['bairro']
    localidade = requests.get(urlViaCep).json()['localidade']
    uf = requests.get(urlViaCep).json()['uf']
    endereco_completo =  f"{logradouro}, {numero}, {complemento}, {cep}, {bairro}, {localidade}, {uf}"
    # CRIA UM NOVO PACIENTE, PASSANDO TODOS OS DADOS DO FORMULÁRIO + ENDEREÇO COMPLETO COMO ARGUMENTO
    novoPaciente = Paciente(request.json['nome'],
                            request.json['doc'],
                            request.json['sexo'],
                            request.json['dtNasc'],
                            endereco_completo,
                            request.json['telefone'],
                            request.json['email'],
                            request.json['convenio'],
                            request.json['senha'])
    # ADICIONA O NOVO PACIENTE AO BANCO DE DADOS
    db.session.add(novoPaciente)
    db.session.commit()
    # REDIRECIONA PARA A ROTA DE LOGIN
    return "True"

# ROTA DE AGENDAMENTO VIA MOBILE
@app.route("/consultas/api", methods=['POST'])
def consultas_api():
    # PEGA O ID DO PACIENTE
    emailPaciente = request.json['paciente']
    pacientes = Paciente.query.all()
    for p in pacientes:
        if p.email == emailPaciente:
            idPaciente = p.idPaciente
    # PEGA O HORARIO
    horaAgendada = request.json['horario']
    # PEGA A DATA, SEPARA E CONVERTE EM OBJETO DATA
    dataAgendada = str(request.json['data'])
    dataAgendada = dataAgendada.split('-')
    dataAgendadaBanco = f'{int(dataAgendada[2])} / {int(dataAgendada[1])} / {int(dataAgendada[0])}'
    dataAgendada = date(int(dataAgendada[0]), int(dataAgendada[1]), int(dataAgendada[2]))
    # PEGA A DATA ATUAL DO USUÁRIO
    dataAtual = date.today()
    # VERIFICA SE A DATA É MAIOR QUE 3 MESES
    if (dataAgendada > (dataAtual + timedelta(days=90))):
        return "False"
    # VERIFICA SE A DATA INFORMA É FINAL DE SEMANA
    if ((dataAgendada.weekday()) == 5 or (dataAgendada.weekday()) == 6):
        return "False"
    # VERIFICA SE A DATA PASSADA PELO FORMULÁRIO É VÁLIDA
    if (dataAgendada < dataAtual or dataAgendada == dataAtual):
        return "False"
    # VERIFICA SE A DATA PASSADO PELO FORMULÁRIO ESTÁ LIVRE
    consultas = Consulta.query.all()
    for c in consultas:
        if ((str(dataAgendadaBanco) == c.data) and (horaAgendada == c.horario)):
            return "False"
    # CRIA UM NOVO AGENDAMENTO
    novoAgendamento = Consulta(idPaciente,
                                horaAgendada,
                                dataAgendadaBanco)
    # ADICIONA O NOVO AGENDAMENTO AO BANCO DE DADOS
    db.session.add(novoAgendamento)
    db.session.commit()
    # REDIRECIONA PARA A ROTA DE CONSULTAS PASSANDO OS DADOS E A MENSAGEM
    return "True"

# ROTA DE LOGIN VIA MOBILE
@app.route("/login/api", methods=['POST'])
def login_api():
    # BUSCA TODOS OS PACIENTES DO BANCO E OS DADOS PASSADOS PELO USUÁRIO
    pacientes = Paciente.query.all()
    email = str(request.json['email'])
    senha = str(request.json['senha'])
    # PERCORRE TODOS OS PACIENTES NA TABELA PACIENTE NO BANCO DE DADOS
    for p in pacientes:
        # SE OS DADOS PASSADOS PELO USUÁRIO CORRESPONDEREM COM ALGUM DO BANCO DE DADOS
        if (email == p.email) and (senha == p.senha):
            # RETORNA VERDADEIRO
            paciente = {"idPaciente": p.idPaciente,
                        "nome": p.nome,
                        "doc": p.doc,
                        "sexo": p.sexo,
                        "dtNasc": p.dtNasc,
                        "endereco": p.endereco,
                        "telefone": p.telefone,
                        "email": p.email,
                        "convenio": p.convenio,
                        "senha": p.senha}
            return "True"
    # RETORNA FALSO
    return "False"
