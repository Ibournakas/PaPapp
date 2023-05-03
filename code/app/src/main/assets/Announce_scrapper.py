import requests
import ssl
import certifi
from bs4 import BeautifulSoup

def Scrapper():

    url = "https://www.ceid.upatras.gr/el/announcement"

    # Path to your SSL certificate and private key files
    cert_file = "certificate.pem"
    key_file = "private_key.pem"


    # Create an SSL context with the certificate and private key
    ssl_context = ssl.create_default_context(purpose=ssl.Purpose.CLIENT_AUTH)
    ssl_context.load_cert_chain(cert_file, key_file)

    # Make the HTTPS request with the SSL context
    response = requests.get(url, verify=False, cert=(cert_file, key_file), headers={'User-Agent': 'Mozilla/5.0'})

    # # Print the status code and content of the response
    # print(response.status_code)
    soup = BeautifulSoup(response.content, "html.parser")

    # Find the total number of pages
    num_pages = int(soup.find('li', {'class': 'pager-last'}).find('a')['href'].split('=')[-1])

    announcements = []
    for i in range(1, min(num_pages+1, 6)):
        # Construct the URL for the current page
        page_url = f"{url}?page={i-1}"

        # Make the HTTPS request with the SSL context
        response = requests.get(page_url, verify=False, cert=(cert_file, key_file), headers={'User-Agent': 'Mozilla/5.0'})
        soup = BeautifulSoup(response.content, "html.parser")

        # Find all the announcement nodes
        announcement_nodes = soup.find_all('article', {'class': 'node-announcement'})

        for node in announcement_nodes:
            date = node.find('div', {'class': 'submitted-date'}).text.strip().replace('\n', ' ')
            title = node.find('h2').text.strip()
            author = node.find('span', {'class': 'username'}).text.strip()
            category = node.find('div', {'class': 'field-name-field-announcement-category'}).text.strip()[10:]
            content = node.find('div', {'class': 'content'}).text.strip()

            announcements.append({'date': date, 'title': title, 'author': author, 'category': category, 'content': content})


    # Print the extracted information
    # with open('announcements.txt', 'w', encoding='utf-8') as file:
    #     for a in announcements:
    #         file.write('Date: {}\n'.format(a['date']))
    #         file.write('Title: {}\n'.format(a['title']))
    #         file.write('Author: {}\n'.format(a['author']))
    #         file.write('Category: {}\n'.format(a['category']))
    #         file.write('Content: {}\n'.format(a['content']))
    #         file.write('--------------------\n')

    # # Print the extracted information
    # for a in announcements:
    #     print('Date: {}'.format(a['date']))
    #     print('Title: {}'.format(a['title']))
    #     print('Author: {}'.format(a['author']))
    #     print('Category: {}'.format(a['category']))
    #     print('Content: {}'.format(a['content']))
    #     print('--------------------')

    # Return the list of announcements
    return announcements