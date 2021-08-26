<?php

    require_once ("../assets/vendor/PHPMailerAutoload.php");

	$ok = "";

	if(isset($_POST["nome"])){

		$assunto = "Clinica";
        $nome = $_POST["nome"];
        $email = $_POST["email"];
        $telefone = $_POST["fone"];
        $mensagem = $_POST["mens"];
		
                    
     
        $phpmail = new PHPMailer(); // Instânciamos a classe PHPmailer para poder utiliza-la          
        $phpmail->isSMTP(); // envia por SMTP
        
        $phpmail->SMTPDebug = 0;
        $phpmail->Debugoutput = 'html';
        
        $phpmail->Host = "smtp.gmail.com"; // SMTP servers         
        $phpmail->Port = 587; // Porta SMTP do GMAIL
        
        //$phpmail->SMTPSecure = 'tls';
        $phpmail->SMTPAuth = true; // Caso o servidor SMTP precise de autenticação   
        
        $phpmail->Username = "devhealthgroup.ads@gmail.com"; // SMTP username         
        $phpmail->Password = "ads2021ope"; // SMTP password          
        $phpmail->IsHTML(true);         
        
        $phpmail->setFrom($email, $nome); // E-mail do remetende enviado pelo method post  
                 
        $phpmail->addAddress("devhealthgroup.ads@gmail.com", "Carolina Ramos");// E-mail do destinatario/*  
        
        $phpmail->Subject = $assunto; // Assunto do remetende enviado pelo method post
                
        $phpmail->msgHTML(" Nome: $nome <br>
                            E-mail: $email <br>
                            Telefone: $telefone <br>
                            Mensagem: $mensagem ");
        
        $phpmail->AlrBody = " Nome: $nome \n
                            E-mail: $email \n
                            Telefone: $telefone\n
                            Mensagem: $mensagem ";
            
        if($phpmail->send()){              
            
            $ok = "OK";
        }else{
			$ok = "NAO";
        }
         
		
        // ############## RESPOSTA AUTOMATICA
        $phpmailResposta = new PHPMailer();        
        $phpmailResposta->isSMTP();
        
        $phpmailResposta->SMTPDebug = 0;
        $phpmailResposta->Debugoutput = 'html';
        
        $phpmailResposta->Host = "smtp.gmail.com";         
        $phpmailResposta->Port = 587;
        
        $phpmailResposta->SMTPSecure = 'tls';
        $phpmailResposta->SMTPAuth = true;   
        
        $phpmailResposta->Username = "devhealthgroup.ads@gmail.com";         
        $phpmailResposta->Password = "ads2021ope";          
        $phpmailResposta->IsHTML(true);         
        
        $phpmailResposta->setFrom($email, $nome); // E-mail do remetende enviado pelo method post  
                 
        $phpmailResposta->addAddress($email, "Carolina Ramos");// E-mail do destinatario/*  
        
        $phpmailResposta->Subject = "Resposta - " .$assunto; // Assunto do remetende enviado pelo method post
                
        $phpmailResposta->msgHTML(" $nome <br>
                            Em breve daremos o retorno");
        
        $phpmailResposta->AlrBody = " $nome \n
                            Em breve daremos o retorno";
            
        $phpmailResposta->send();
        
    } // FECHAR O IF  

?>



<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/contact.css">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display&display=swap" rel="stylesheet">
    <title>Contato</title>
</head>
<body>

    <!-- CABEÇALHO - MENU -->
    <header class="header">
        <div class="menu">
            <img src="../assets/images/logos/logo.png" alt="">
            <ul>
                <li>
                    <a href="../index.html">INICÍO</a>
                </li>
                <li>
                    <a href="">AGENDAMENTO</a>
                </li>
                <li>
                    <a href="">ARTIGOS</a>
                </li>
                <li>
                    <a href="">CONTATO</a>
                </li>
            </ul>
        </div>
    </header>


    <main>
        <!-- SEÇÃO 1 - FORMULÁRIO -->
        <div class="formulario"> 
            <h2>ENVIE UMA MENSAGEM</h2>
            <form action="#" method="post" class="form">
                <div class="item">
                    <input type="text" placeholder="Digite seu nome:" name="nome">
                </div>
                <div class="item">
                    <input type="email" placeholder="Digite seu email:" name="email">
                </div>
                <div class="item">
                    <input type="text" placeholder="Digite seu telefone:" name="fone">
                </div>
                <div class="item">
                    <textarea cols="30" rows="10" placeholder="Digite sua mensagem:" name="mens"></textarea>
                </div>
				<?php
					if($ok == "OK"){
						echo "$nome, A Mensagem foi enviada com sucesso.";
					}else if($ok == "NAO"){
						echo "$nome, Não foi possível enviar a mensagem. Erro: " .$phpmail->ErrorInfo;
										
					}					
				?>
                <div class="enviar">
                    <button type="submit">ENVIAR</button>
                </div>
            </form>
        </div>   
        
        

        <!-- SEÇÃO 2 - MAP -->
        <section>
            <div class="map">
                <h2>LOCALIZAÇÃO</h2>
                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3654.361134559922!2d-46.52900554997021!3d-23.663039971163517!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x94ce42881c10299b%3A0x78181147619d3753!2sR.%20Cel.%20Agenor%20de%20Camargo%2C%2081%20-%20Centro%2C%20Santo%20Andr%C3%A9%20-%20SP%2C%2009020-220!5e0!3m2!1spt-BR!2sbr!4v1614384230701!5m2!1spt-BR!2sbr" width="100%" height="400" style="border:0;" allowfullscreen="" loading="lazy"></iframe>
            </div>
        </section>

    </main>

    

    <!-- RODAPÉ -->
    <footer>
        <div class="footer">
            <div class="rodapeEnd">
                <article>
                    <p>Rua Coronel Agenor de Camargo, 81<br>
                        Centro - Santo André, SP<br>
                        (11) 984292-0089<br>
                        example@example.com.br</p>
                </article>
                <article class="rodapeRede">
                    <a href="https://www.f.com/" target="_blank">
                        <img src="../assets/images/logos/facebooklogo.png" alt="facebook logo">
                    </a>
                    <a href="https://www.i.com" target="_blank">
                        <img src="../assets/images/logos/instragramlogo.png" alt="instagram logo">
                    </a>
                    <a href="https://www.linkedin.com/in/carolina-bertolino-68aa373b/" target="_blank">
                        <img src="../assets/images/logos/linkedin.png" alt="linkedin logo">
                    </a>
                    <a href="https://wa.me/+5511984920089" target="_blank">
                        <img src="../assets/images/logos/whatsapplogo.png" alt="whatsapp logo">
                    </a>
                </article>
            </div>
            <div class="copy">
                <p>Copyright &copy; 2021 Carolina Ramos Bertolino</p>
            </div>
        </div>
    </footer>
</body>
</html>
