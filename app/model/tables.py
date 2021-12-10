from app import app, db


# CRIAÇÃO DA TABELA PACIENTE
class Paciente(db.Model):
    __tablename__ = "paciente"
    idPaciente = db.Column(db.Integer, autoincrement=True, primary_key=True)
    doc = db.Column(db.Integer)
    nome = db.Column(db.String(70))
    sexo = db.Column(db.String(10))
    dtNasc = db.Column(db.String(10))
    endereco = db.Column(db.String(50))
    email = db.Column(db.String(30))
    telefone = db.Column(db.String(11))
    convenio = db.Column(db.Integer)
    senha = db.Column(db.String(20))
    arquivado = db.Column(db.Integer)

    # CONSTRUTOR DA TABELA PACIENTE
    def __init__(self, nome, doc, sexo, dtNasc, endereco, telefone, email, convenio, senha):
        self.doc = doc
        self.nome = nome
        self.sexo = sexo
        self.dtNasc = dtNasc
        self.endereco = endereco
        self.telefone = telefone
        self.email = email
        self.convenio = convenio
        self.senha = senha
        self.arquivado = 0


# CRIAÇÃO DA TABELA CONSULTA
class Consulta(db.Model):
    __tablename__ = "consulta"
    idConsulta = db.Column(db.Integer, autoincrement=True, primary_key=True)
    paciente = db.Column(db.Integer)
    horario = db.Column(db.String(70))
    data = db.Column(db.String(70))
    status = db.Column(db.String(20))

    # CONSTRUTOR DA TABELA CONSULTA
    def __init__(self, paciente, horario, data):
        self.paciente = paciente
        self.horario = horario
        self.data = data
        self.status = "Aguardando aprovação"


# CRIAÇÃO DA TABELA PAGAMENTO
class Pagamento(db.Model):
    __tablename__ = "pagamento"
    idPagamento = db.Column(db.Integer, autoincrement=True, primary_key=True)
    consulta = db.Column(db.Integer)
    paciente = db.Column(db.Integer)
    valor = db.Column(db.String(30))
    status = db.Column(db.String(30))
    convenio = db.Column(db.Integer)

    # CONSTRUTOR DA TABELA PAGAMENTO
    def __init__(self, paciente, consulta, valor, convenio):
        self.paciente = paciente
        self.consulta = consulta
        self.valor = valor
        self.status = 'Pendente'
        self.convenio = convenio