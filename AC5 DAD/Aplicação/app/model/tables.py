from app import app, db

class Paciente(db.Model):
    __tablename__ = "paciente"
    cpf = db.Column(db.Integer, primary_key=True)
    nome = db.Column(db.String(70))
    sexo = db.Column(db.String(10))
    dtNasc = db.Column(db.String(10))
    endereco = db.Column(db.String(50))
    email = db.Column(db.String(30))
    telefone = db.Column(db.String(11))
    tipoPlano = db.Column(db.String(10))
    senha = db.Column(db.String(20))

    def __init__(self, cpf, nome, sexo, dtNasc, endereco, email, telefone, tipoPlano, senha):
        self.cpf = cpf
        self.nome = nome
        self.sexo = sexo
        self.dtNasc = dtNasc
        self.endereco = endereco
        self.email = email
        self.telefone = telefone
        self.tipoPlano = tipoPlano
        self.senha = senha


class Consulta(db.Model):
    __tablename__ = "consulta"
    cpfPaciente = db.Column(db.Integer, primary_key=True)
    horario = db.Column(db.String(70))
    data = db.Column(db.String(70))
    status = db.Column(db.String(20))

    def __init__(self, cpfPaciente, horario, data, status):
        self.cpf = cpfPaciente
        self.horario = horario
        self.data = data
        self.status = status


class Pagamento(db.Model):
    __tablename__ = "pagamento"
    cpfPaciente = db.Column(db.Integer, primary_key=True)
    valor = db.Column(db.String(10))
    tipo = db.Column(db.String(20))

    def __init__(self, valor, tipo):
        self.valor = valor
        self.tipo = tipo


class Mensagem(db.Model):
    __tablename__ = "mensagem"
    idMensagem = db.Column(db.Integer, primary_key= True)
    assunto = db.Column(db.String(20))
    conteudo = db.Column(db.String(120))
    cpf_Carol = db.Column(db.String(10))
    cpf = db.Column(db.Integer)

    def __init__(self, assunto, conteudo, cpf):
        self.assunto = assunto
        self.conteudo = conteudo
        self.cpf = cpf
        self.cpf_Carol = '0001'