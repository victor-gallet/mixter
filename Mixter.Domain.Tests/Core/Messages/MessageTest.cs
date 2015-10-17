using System;
using System.Collections.Generic;
using System.Linq;
using Mixter.Domain.Core.Messages;
using Mixter.Domain.Core.Messages.Events;
using Mixter.Domain.Identity;
using NFluent;
using Xunit;

namespace Mixter.Domain.Tests.Core.Messages
{
    public class MessageTest
    {
        private const string MessageContent = "Hello miixit";

        private static readonly UserId Author = new UserId("pierre@mixit.fr");
        private static readonly UserId Requacker = new UserId("alfred@mixit.fr");
        private static readonly MessageId MessageId = MessageId.Generate();

        private readonly EventPublisherFake _eventPublisher;

        public MessageTest()
        {
            _eventPublisher = new EventPublisherFake();
        }

        [Fact]
        public void WhenQuackMessageThenRaiseUserMessageQuacked()
        {
            Message.Quack(_eventPublisher, Author, MessageContent);

            var evt = (MessageQuacked)_eventPublisher.Events.First();
            Check.That(evt.Content).IsEqualTo(MessageContent);
            Check.That(evt.Author).IsEqualTo(Author);
        }

        [Fact]
        public void WhenQuackSeveralMessageThenMessageIdIsNotSame()
        {
            Message.Quack(_eventPublisher, Author, MessageContent);
            Message.Quack(_eventPublisher, Author, MessageContent);

            var events = _eventPublisher.Events.OfType<MessageQuacked>().ToArray();
            Check.That(events[0].Id).IsNotEqualTo(events[1].Id);
        }

        [Fact]
        public void WhenQuackMessageThenReturnMessageId()
        {
            var messageId = Message.Quack(_eventPublisher, Author, MessageContent);

            var evt = (MessageQuacked)_eventPublisher.Events.First();
            Check.That(evt.Id).IsEqualTo(messageId);
        }

        [Fact]
        public void WhenRequackMessageThenRaiseMessageRequacked()
        {
            Given(new MessageQuacked(MessageId, Author, MessageContent))
            .When(o => o.Requack(_eventPublisher, Requacker))
            .ThenHas(new MessageRequacked(MessageId, Requacker));
        }

        [Fact]
        public void WhenRequackMyOwnMessageThenDoNotRaiseMessageRequacked()
        {
            Given(new MessageQuacked(MessageId, Author, MessageContent))
            .When(o => o.Requack(_eventPublisher, Author))
            .ThenNothing();
        }

        [Fact]
        public void WhenRequackTwoTimesSameMessageThenDoNotRaiseMessageRequacked()
        {
            Given(new MessageQuacked(MessageId, Author, MessageContent))
                .And(new MessageRequacked(MessageId, Requacker))
            .When(o => o.Requack(_eventPublisher, Requacker))
            .ThenNothing();
        }

        [Fact]
        public void WhenDeleteThenRaiseMessageDeleted()
        {
            Given(new MessageQuacked(MessageId, Author, MessageContent))
            .When(o => o.Delete(_eventPublisher, Author))
            .ThenHas(new MessageDeleted(MessageId, Author));
        }

        private GivenFactory Given(IDomainEvent evt)
        {
            return new GivenFactory(evt, _eventPublisher);
        }

        private class GivenFactory
        {
            private readonly IList<IDomainEvent> _events = new List<IDomainEvent>();
            private readonly EventPublisherFake _eventPublisherFake;

            public GivenFactory(IDomainEvent evt, EventPublisherFake eventPublisherFake)
            {
                _events.Add(evt);
                _eventPublisherFake = eventPublisherFake;
            }

            public ThenFactory When(Action<Message> when)
            {
                var message = new Message(_events);
                when(message);

                return new ThenFactory(_eventPublisherFake);
            }

            public GivenFactory And(IDomainEvent evt)
            {
                _events.Add(evt);

                return this;
            }

            public class ThenFactory
            {
                private readonly EventPublisherFake _eventPublisherFake;

                public ThenFactory(EventPublisherFake eventPublisherFake)
                {
                    _eventPublisherFake = eventPublisherFake;
                }

                public void ThenHas(IDomainEvent domainEvent)
                {
                    Check.That(_eventPublisherFake.Events).Contains(domainEvent);
                }

                public void ThenNothing()
                {
                    Check.That(_eventPublisherFake.Events).IsEmpty();
                }
            }
        }
    }
}
